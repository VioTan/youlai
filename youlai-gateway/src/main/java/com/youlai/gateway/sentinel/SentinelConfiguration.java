package com.youlai.gateway.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.core.common.result.ResultCodeEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.annotation.PostConstruct;

/**
 * @author:GSHG
 * @date: 2021-09-15 10:55
 * description:
 */
@Configuration
public class SentinelConfiguration {

    @PostConstruct
    private void initBlockHandler(){
        BlockRequestHandler blockRequestHandler = (exchange,t) ->
                ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ResultCodeEnum.FLOW_LIMITING.toString()));
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }

}
