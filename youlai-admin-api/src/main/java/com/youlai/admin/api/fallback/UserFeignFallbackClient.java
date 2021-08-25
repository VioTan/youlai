package com.youlai.admin.api.fallback;

import com.core.common.result.Result;
import com.core.common.result.ResultCodeEnum;
import com.youlai.admin.api.service.UserFeignClient;
import com.youlai.admin.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author:GSHG
 * @date: 2021-08-24 15:01
 * description:
 */
@Component
@Slf4j
public class UserFeignFallbackClient implements UserFeignClient {


    @Override
    public Result<SysUser> getUserByUsername(String username) {

        log.error("feign远程调用系统用户服务异常后的降级方法");
        return Result.failed(ResultCodeEnum.DEGRADATION);
    }
}
