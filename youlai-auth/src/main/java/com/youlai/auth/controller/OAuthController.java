package com.youlai.auth.controller;

import com.core.common.result.Result;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.youlai.admin.api.service.UserFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * @author:GSHG
 * @date: 2021-08-16 12:48
 * description:
 */

@Api(tags = "认证登录中心")
@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
@Slf4j
public class OAuthController {

  //  private KeyPair keyPair;

//    @ApiOperation(value = "获取公钥",notes = "login")
//    @GetMapping("/getPublicKey")
//    public Map<String, Object> getPublicKey(){
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAKey key = new RSAKey.Builder(publicKey).build();
//        return new JWKSet(key).toJSONObject();
//    }
//
//    private UserFeignClient userFeignClient;
//
//    @GetMapping("/loadUserByUsername")
//    public Result loadUserByUsername(){
//        Result userDTO = userFeignClient.getUserByUsername("admin");
//        return userDTO;
//    }

}
