package com.youlai.admin.component.security;

import com.youlai.admin.service.ISysPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 容器启动完成时加载角色权限规则至redis缓存
 * @author:GSHG
 * @date: 2021-09-01 11:33
 * description:
 */
@Component
@AllArgsConstructor
public class InitPermissionRoles implements CommandLineRunner {

    private ISysPermissionService iSysPermissionService;

    @Override
    public void run(String... args) throws Exception {
            iSysPermissionService.refreshPermRolesRules();
    }
}
