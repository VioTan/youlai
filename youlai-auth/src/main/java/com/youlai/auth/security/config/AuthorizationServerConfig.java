package com.youlai.auth.security.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.core.common.result.Result;
import com.core.common.result.ResultCodeEnum;
import com.youlai.auth.domain.OAuthUserDetails;
import com.youlai.auth.security.service.ClientDetailsServiceImpl;
import com.youlai.auth.security.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 认真授权配置
 *
 * 配置OAuth2认证允许接入的客户端的信息
 *我们需要把客户端信息配置在认证服务器上来表示认证服务器所认可的客户端。一般可配置在认证服务器的内存中，但是这样很不方便管理扩展。
 * 所以实际最好配置在数据库中的，提供可视化界面对其进行管理，方便以后像PC端、APP端、小程序端等多端灵活接入。
 * @author:GSHG
 * @date: 2021-08-26 9:55
 * description:
 */
@Configuration
@EnableAuthorizationServer
@AllArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

        private AuthenticationManager authenticationManager;
        private UserDetailsServiceImpl userDetailsService;
        private ClientDetailsServiceImpl clientDetailsService;

        /**
         * OAuth2客户端【数据库加载】
         */
        @Override
        @SneakyThrows
        public void configure(ClientDetailsServiceConfigurer configurer){
            configurer.withClientDetails(clientDetailsService);
        }

        /**
         * 配置授权（authorization）以及令牌（token）的访问断点和令牌服务(token services)
         * @param endpoints
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints){
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
            //通过userId和username  JWT内容增强  使用匿名方法返回accessToken
            tokenEnhancers.add(tokenEnhancer());
            //使用非对称加密算法对token签名 +  公钥 + 私钥
            tokenEnhancers.add(jwtAccessTokenConverter());
            tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

            endpoints.authenticationManager(authenticationManager)
                     .accessTokenConverter(jwtAccessTokenConverter())
                    .tokenEnhancer(tokenEnhancerChain)
                    .userDetailsService(userDetailsService)
                    // refresh token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
                    //      1 重复使用：access token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
                    //      2 非重复使用：access token过期刷新时， refresh token过期时间延续，在refresh token有效期内刷新便永不失效达到无需再次登录的目的
                    .reuseRefreshTokens(true);

        }

        /**
         * 使用非对称加密算法对token签名
         *
         * token生成器 可以实现指定token的生成方式(JWT)和对JWT进行签名
         * 两种方式：
         * 对称方式：认证服务器和资源服务器使用同一个密钥进行加签和验签 ，默认算法HMAC
         * 非对称方式：认证服务器使用私钥加签，资源服务器使用公钥验签，默认算法RSA
         *
         * @return
         */
        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter(){
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setKeyPair(keyPair());
            return converter;

        }

        /**
         * 使用cmd命令生产  oauth2秘钥
         * 从classpath下的秘钥库中获取秘钥对(公钥 + 私钥)
         * (1). 从密钥库获取密钥对(密钥+私钥)
         * (2). 认证服务器私钥对token签名
         * (3). 提供公钥获取接口供资源服务器验签使用
         * 使用JDK工具的keytool生成JKS密钥库(Java Key Store)，并将youlai.jks放到resources目录
         *
         * 在windows生成命令：keytool -genkey -alias youlai -keyalg RSA -keypass 123456 -keystore youlai.jks -storepass 123456
         *
         * 生成公共秘钥：
         *  keytool -list -rfc --keystore ffzs-jwt.jks | openssl x509 -inform pem -pubkey
         *
         *
         *-genkey 生成密钥
         *
         * -alias 别名
         *
         * -keyalg 密钥算法
         *
         * -keypass 密钥口令
         *
         * -keystore 生成密钥库的存储路径和名称
         *
         * -storepass 密钥库口令
         */
        @Bean
        public KeyPair keyPair(){
            KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"),"123456".toCharArray());
            KeyPair keyPair = factory.getKeyPair("jwt","123456".toCharArray());
            return keyPair;
        }

    /**
     * JWT内容增强  通过userId和username
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer(){
        /**
         * 使用匿名方法返回accessToken
         */
        return (accessToken,authentication) ->{
            Map<String,Object> additionalInfo = CollectionUtil.newHashMap();
            OAuthUserDetails oAuthUserDetails = (OAuthUserDetails) authentication.getUserAuthentication().getPrincipal();
            additionalInfo.put("userId",oAuthUserDetails.getId());
            additionalInfo.put("username",oAuthUserDetails.getUsername());
            ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false); // 用户不存在异常抛出
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return  provider;
    }

    /**
     * 密码编码器
     *
     * 委托方式，根据密码的前缀选择对应的encode,例如：{bcypt}标识BCYPT算法加密；{noop}->标识不使用任何加密即明文的方式
     * 密码判读 DaoAuthenticationProvider#additionalAuthenticationChecks
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return ((request, response, authException) -> {
            response.setStatus(HttpStatus.HTTP_OK);
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            //客户端认真失败
            Result result = Result.failed(ResultCodeEnum.CLIENT_AUTHENTICATION_FAILED);
            response.getWriter().print(JSONUtil.toJsonStr(result));
            response.getWriter().flush();

        });
    }

}
