package com.youlai.admin.api.service;

import com.core.common.result.Result;
import com.youlai.admin.api.pojo.entity.SysOauthClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "youlai-admin",contextId = "oauth-client")
public interface OAuthClientFeignClient {

    @GetMapping("api/oauth-clients/{clientId}")
    Result<SysOauthClient> getOAuthClientById(@PathVariable String clientId);

}
