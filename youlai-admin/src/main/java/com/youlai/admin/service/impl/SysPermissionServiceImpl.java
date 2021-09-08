package com.youlai.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.constant.GlobalConstants;
import com.youlai.admin.api.pojo.entity.SysPermission;
import com.youlai.admin.mapper.SysPermissionMapper;
import com.youlai.admin.service.ISysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:GSHG
 * @date: 2021-09-01 11:35
 * description:
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper,SysPermission> implements ISysPermissionService {
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public List<SysPermission> listPermRoles() {
        return this.baseMapper.listPermRoles();
    }

    @Override
    public boolean refreshPermRolesRules() {
        //删除redis的键
        redisTemplate.delete(Arrays.asList(GlobalConstants.URL_PERM_ROLES_KEY,GlobalConstants.BTN_PERM_ROLES_KEY));
        //从数据库中获取权限
        List<SysPermission> permissions = this.listPermRoles();
        if(CollectionUtil.isNotEmpty(permissions)){
            // 初始化URL【权限->角色(集合)】规则
           List<SysPermission> urlPermList = permissions.stream().filter(item ->
                StrUtil.isNotBlank(item.getUrlPerm()))
                .collect(Collectors.toList());
           if(CollectionUtil.isNotEmpty(urlPermList)){
               Map<String,List<String>> urlPermRoles = new HashMap<>();
               urlPermList.stream().forEach(item -> {
                   //获取角色权限路径
                   String perm = item.getUrlPerm();
                   //获取角色
                   List<String> roles = item.getRoles();
                   urlPermRoles.put(perm,roles);
               });
               //讲权限集合放到redis
               redisTemplate.opsForHash().putAll(GlobalConstants.URL_PERM_ROLES_KEY,urlPermRoles);
               redisTemplate.convertAndSend("cleanRoleLocalCache",true);
           }

            // 初始化URL【按钮->角色(集合)】规则
            List<SysPermission> btnPermList = permissions.stream().filter(item -> StrUtil.isNotBlank(item.getBtnPerm()))
                    .collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(btnPermList)){
                Map<String,List<String>> btnPermRoles = CollectionUtil.newHashMap();
                btnPermList.stream().forEach(item -> {
                    String perm = item.getBtnPerm();
                    List<String> roles = item.getRoles();
                    btnPermRoles.put(perm, roles);
                });
                //将按钮角色按钮放到redis
                redisTemplate.opsForHash().putAll(GlobalConstants.BTN_PERM_ROLES_KEY,btnPermRoles);

            }


        }

        return true;
    }



}
