package com.youlai.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.result.Result;
import com.youlai.admin.api.pojo.entity.SysUser;
import com.youlai.admin.api.pojo.entity.SysUserRole;
import com.youlai.admin.service.ISysUserRoleService;
import com.youlai.admin.service.ISysUserService;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author:GSHG
 * @date: 2021-08-19 11:14
 * description:
 */
@Api(tags = " 用户接口 ")
@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;

    @ApiOperation(value = "列表分页",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "limit", value = "每页数量", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "nickname", value = "用户昵称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机号码", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "deptId", value = "部门ID", paramType = "query", dataType = "Long"),
    })
    @GetMapping
    private Result list(       Integer page,
                               Integer limit,
                               String nickname,
                               String mobile,
                               Integer status,
                               Long deptId){
        SysUser user = new SysUser();
        user.setNickname(nickname);
        user.setMobile(mobile);
        user.setStatus(status);
        user.setDeptId(deptId);

        IPage<SysUser> result = iSysUserService.list(new Page<>(page,limit),user);

        return Result.success(result.getRecords(),result.getTotal());
    }

    @ApiOperation(value = "用户详情")
    @ApiImplicitParam(name = "id",value = "用户id",required = true,paramType = "path",dataType = "Long")
    @GetMapping("/{id}")
    public  Result detail(@PathVariable  Long id){
       SysUser user = iSysUserService.getById(id);
       if(user != null){
           List<Long> roleIds = iSysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>()
           .eq(SysUserRole::getRoleId,user.getId())
                   .select(SysUserRole::getRoleId)
           ).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
           user.setRoleIds(roleIds);
       }

        return Result.success(user);
    }

    @ApiOperation(value = "根据用户名获取用户信息")
    @ApiImplicitParam(name = "username",value = "用户名",required = true,paramType = "path",dataType = "String")
    @GetMapping("username/{username}")
    public Result<SysUser> getUserByUsername(@PathVariable String username){

       SysUser user = iSysUserService.getByUsername(username);

        return Result.success(user);
    }

}
