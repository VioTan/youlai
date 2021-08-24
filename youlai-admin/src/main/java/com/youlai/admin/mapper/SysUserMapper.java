package com.youlai.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youlai.admin.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author:GSHG
 * @date: 2021-08-19 11:58
 * description:
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser getByUsername(String username);
}
