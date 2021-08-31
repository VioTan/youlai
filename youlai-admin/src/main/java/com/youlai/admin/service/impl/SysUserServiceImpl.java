package com.youlai.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youlai.admin.api.pojo.entity.SysUser;
import com.youlai.admin.mapper.SysUserMapper;
import com.youlai.admin.service.ISysUserService;
import org.springframework.stereotype.Service;

/**
 * @author:GSHG
 * @date: 2021-08-19 11:58
 * description:
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {



    @Override
    public SysUser getByUsername(String username) {
        return this.baseMapper.getByUsername(username);
    }
}
