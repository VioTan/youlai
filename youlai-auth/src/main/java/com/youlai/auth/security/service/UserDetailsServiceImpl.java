package com.youlai.auth.security.service;

import com.core.common.result.Result;
import com.core.common.result.ResultCodeEnum;
import com.youlai.admin.api.service.UserFeignClient;
import com.youlai.admin.api.pojo.entity.SysUser;
import com.youlai.auth.common.enums.OAuthClientEnum;
import com.youlai.auth.domain.OAuthUserDetails;
import com.youlai.common.web.util.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author:GSHG
 * @date: 2021-08-26 9:58
 * description:
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    /**
     * 获取调用Feign接口获取用户(角色)
     */
    private UserFeignClient userFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从请求头或者路径中获取 clientId
        String clientId = JwtUtils.getOAuthClientId();

        //通过客户端id获取  授权对象
        OAuthClientEnum client = OAuthClientEnum.getByClientId(clientId);

        Result result;
        OAuthUserDetails oAuthUserDetails = null;
        switch (client){
            default:
                result = userFeignClient.getUserByUsername(username);
                if(ResultCodeEnum.SUCCESS.getCode().equals(result.getCode())){
                   SysUser sysUser = (SysUser) result.getData();
                   oAuthUserDetails = new OAuthUserDetails(sysUser);
                }
                break;
        }

        if(oAuthUserDetails == null || oAuthUserDetails.getId() == null){
            throw  new UsernameNotFoundException(ResultCodeEnum.USER_NOT_EXIST.getMsg());
        }else if(!oAuthUserDetails.isEnabled()){
            throw new DisabledException("该账户被禁用!");
        }else if(!oAuthUserDetails.isAccountNonLocked()){
            throw new LockedException("该账号被锁定");
        }else if (!oAuthUserDetails.isAccountNonLocked()){
            throw new AccountExpiredException("该账户已过期");
        }



        return oAuthUserDetails;
    }
}
