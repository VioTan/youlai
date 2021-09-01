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

    List<SysPermission> listPermRoles();

    boolean refreshPermRolesRules();

}
