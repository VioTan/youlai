package com.youlai.auth.controller;

import com.core.common.result.Result;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.youlai.admin.api.service.UserFeignClient;
import com.youlai.auth.common.enums.OAuthClientEnum;
import com.youlai.common.web.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.Principal;
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

    private TokenEndpoint tokenEndpoint;

    private KeyPair keyPair;

    @ApiOperation(value = "OAuth2认证",notes = "login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type",defaultValue = "password",value = "授权模式",required = true),
            @ApiImplicitParam(name = "client_id",value = "Oauth2客户端ID（新版本需放置请求头）",required = true),
            @ApiImplicitParam(name = "client_secret",value = "Oauth2客户端秘钥（新版本需放置请求头）",required = true),
            @ApiImplicitParam(name = "refresh_token",value = "刷新token"),
            @ApiImplicitParam(name = "username",defaultValue = "admin",value = "登录用户名"),
            @ApiImplicitParam(name = "password",defaultValue = "123456",value = "登录密码")
    })
    @PostMapping("/token")
    public  Object posrAssessToken(Principal principal,Map<String,String> parameters) throws HttpRequestMethodNotSupportedException {
        String clentId = JwtUtils.getOAuthClientId();
        OAuthClientEnum client = OAuthClientEnum.getByClientId(clentId);
        switch (client){
            case TEST: // knife4j接口测试文档使用 client_id/client_secret : client/123456
                return tokenEndpoint.postAccessToken(principal,parameters);
                default:
                    return Result.success(tokenEndpoint.postAccessToken(principal,parameters).getBody());
        }
    }


    @ApiOperation(value = "获取公钥",notes = "login")
    @GetMapping("/public-key")
    public Map<String, Object> getPublicKey(){
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

//    private UserFeignClient userFeignClient;
//    @GetMapping("/loadUserByUsername")
//    public Result loadUserByUsername(){
//        Result userDTO = userFeignClient.getUserByUsername("admin");
//        return userDTO;
//    }

}
