package com.youlai.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youlai.admin.api.pojo.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Eric
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    /**
     * 获取所有角色的权限 crud
     * @return
     */
    List<SysPermission> listPermRoles();


}
