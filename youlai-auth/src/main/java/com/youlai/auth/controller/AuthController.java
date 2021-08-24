package com.youlai.auth.controller;

import com.core.common.result.Result;
import com.youlai.admin.api.UserFeignClient;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:GSHG
 * @date: 2021-08-16 12:48
 * description:
 */
@RefreshScope
@RestController
@Api(tags = "认证登录中心")
@RequestMapping("/oauth")
public class AuthController {

    @Value("${rsa.publicKey}")
     public String publicKey;

    @GetMapping("/getPublicKey")
    public Result getPublicKey(){
        return Result.success(this.publicKey);
    }

    private UserFeignClient userFeignClient;


}
