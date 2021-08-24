package com.youlai.admin.api;

import com.youlai.admin.api.fallback.UserFeignFallbackClient;
import com.core.common.result.Result;
import com.youlai.admin.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "youlai-admin",fallback = UserFeignFallbackClient.class)
public interface UserFeignClient {
    @GetMapping("/api/v1/users/username/{username}")
    Result<SysUser> getUserByUsername(@PathVariable String username);

}
