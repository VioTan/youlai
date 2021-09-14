package com.youlai.gateway.security;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import com.core.common.constant.AuthConstants;
import com.core.common.result.ResultCodeEnum;
import com.youlai.gateway.util.ResponseUtils;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

/**
 * 资源服务器配置
 * 将鉴权管理器AuthorizationManager配置到资源服务器、请求白名单放行、无权访问和无效token的自定义异常响应
 * @author:GSHG
 * @date: 2021-09-01 14:10
 * description:
 */
@ConfigurationProperties(prefix = "security")
@AllArgsConstructor
@Configuration
//注解需要使用@EnableWebFluxSecurity而非@EnableWebSecurity,因为SpringCloud Gateway基于WebFlux
@EnableWebFluxSecurity
@Component
public class ResourceServerConfig {

        private ResourceServerManager resourceServerManager;

        @Setter
        private List<String> ignoreUrls;

        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
            http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())
                    .publicKey(rsaPublicKey()) //本地获取公钥
                    //.jwkSetUri() 远程获取公钥
            ;
            http.oauth2ResourceServer().authenticationEntryPoint(authenticationEntryPoint());
            http.authorizeExchange()
                    .pathMatchers(Convert.toStrArray(ignoreUrls)).permitAll()
                    .anyExchange().access(resourceServerManager)
                    .and()
                    .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler()) //处理未授权
                    .authenticationEntryPoint(authenticationEntryPoint()) //处理未认证
                    .and().csrf().disable();

            return http.build();

        }

    /**
     * @link https://blog.csdn.net/qq_24230139/article/details/105091273
     * ServerHttpSecurity没有将jwt中authorities的负载部分当做Authentication
     *  需要把jwt的Claim中的authorities加入
     *  方案：重新定义权限管理器，默认转换器JwtGrantedAuthoritiesConverter
     *
     * @return
     */
    @Bean
    public Converter<Jwt,? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter(){
            JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
            jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstants.AUTHORITY_PREFIX);
            jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstants.JWT_AUTHORITIES_KEY);

            JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
            jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

            return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);

        }

    /**
     *      * @URL: http://slproweb.com/products/Win32OpenSSL.html
     *      * 需要安装openSSL生成校验的公共令牌
     *
     *  本地获取JWT验签公钥
     * @return
     */
        @Bean
        @SneakyThrows
        public RSAPublicKey rsaPublicKey(){
        Resource resource = new ClassPathResource("public.key");
        InputStream is = resource.getInputStream();
        String publicKeyData = IoUtil.read(is).toString();

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKeyData));
       KeyFactory keyFactory =  KeyFactory.getInstance("RSA");
      RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
            return rsaPublicKey;
        }

    /**
     * token无效或者已过期  自定义响应
     * @return
     */
    @Bean
    ServerAuthenticationEntryPoint authenticationEntryPoint(){
        return ((exchange, ex) -> {
           Mono<Void> mono = Mono.defer(() -> Mono.just(exchange.getResponse()))
                    .flatMap(response -> ResponseUtils.writeErrorInfo(response, ResultCodeEnum.TOKEN_INVALID_OR_EXPIRED));
            return mono;

        });
        }

    /**
     * 未授权 自定义响应
     */
    @Bean
    ServerAccessDeniedHandler  accessDeniedHandler(){
        return (exchange, denied) -> {
           Mono<Void> mono = Mono.defer(() -> Mono.just(exchange.getResponse()))
                    .flatMap(response -> ResponseUtils.writeErrorInfo(response,ResultCodeEnum.ACCESS_UNAUTHORIZED));
            return mono;

        };
    }



}
