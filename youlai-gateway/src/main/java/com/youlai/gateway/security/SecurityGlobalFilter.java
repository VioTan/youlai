package com.youlai.gateway.security;

import cn.hutool.core.util.StrUtil;
import com.core.common.constant.AuthConstants;
import com.core.common.result.ResultCodeEnum;
import com.youlai.gateway.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author:GSHG
 * @date: 2021-09-14 16:00
 * description:
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityGlobalFilter implements GlobalFilter, Ordered {

    private final RedisTemplate redisTemplatel;
    @Value("${spring.profiles.active}")
    private String env;


    @Override
    @SneakyThrows
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 线上演示环境禁止修改和删除
        if("prod".equals(env) && (HttpMethod.DELETE.toString().equals(request.getMethodValue())) || HttpMethod.PUT.toString().equals(request.getMethodValue()) ){
                return ResponseUtils.writeErrorInfo(response, ResultCodeEnum.FORBIDDEN_OPERATION);
        }

        // 非JWT或者JWT为空不作处理
        String token = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION_KEY);
        if(StrUtil.isBlank(token) || token.startsWith(AuthConstants.AUTHORIZATION_PREFIX)){

        }


        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
