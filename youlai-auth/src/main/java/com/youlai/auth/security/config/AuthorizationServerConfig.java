package com.youlai.auth.security.config;

import cn.hutool.core.collection.CollectionUtil;
import com.youlai.auth.domain.OAuthUserDetails;
import com.youlai.auth.security.service.ClientDetailsServiceImpl;
import com.youlai.auth.security.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 认真授权配置
 *
 * @author:GSHG
 * @date: 2021-08-26 9:55
 * description:
 */
@Configuration
@EnableAutoConfiguration
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

    /**
     * 使用非对称加密算法对token签名
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair());
        return converter;

    }

    /**
     * 从classpath下的秘钥库中获取秘钥对(公钥 + 私钥)
     */
    @Bean
    public KeyPair keyPair(){
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"),"123456".toCharArray());
       KeyPair keyPair = factory.getKeyPair("jwt","123456".toCharArray());
       return keyPair;
    }

}
