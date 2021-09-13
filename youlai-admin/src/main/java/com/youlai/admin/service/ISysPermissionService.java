package com.youlai.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.youlai.admin.api.pojo.entity.SysPermission;

import java.util.List;

/**
 * @author:GSHG
 * @date: 2021-09-01 11:35
 * description:
 */
public interface ISysPermissionService extends IService<SysPermission> {
    /**
     * 获取角色所拥有的权限 crud增删改查
     * @return
     */
    List<SysPermission> listPermRoles();

    /**
     *
     * @return
     */
    boolean refreshPermRolesRules();

}
