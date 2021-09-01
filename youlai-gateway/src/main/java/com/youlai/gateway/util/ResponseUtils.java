package com.youlai.gateway.util;

import cn.hutool.json.JSONUtil;
import com.core.common.result.Result;
import com.core.common.result.ResultCodeEnum;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * 自定义响应
 * @author:GSHG
 * @date: 2021-09-01 15:02
 * description:
 */
public class ResponseUtils {

        public static Mono writeErrorInfo(ServerHttpResponse response, ResultCodeEnum resultCodeEnum){
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().set("Access-Control-Allow-Origin","*");
            response.getHeaders().set("Cache-Control","no-cache");
           String body = JSONUtil.toJsonStr(Result.failed(resultCodeEnum));
           DataBuffer buffer =  response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
            return response.writeWith(Mono.just(buffer))
                    .doOnError(error -> DataBufferUtils.release(buffer));



        }

}
