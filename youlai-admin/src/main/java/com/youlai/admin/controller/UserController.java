package com.youlai.admin.controller;

import com.core.common.result.Result;
import com.youlai.admin.api.pojo.entity.SysUser;
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



/**
 * @author:GSHG
 * @date: 2021-08-19 11:14
 * description:
 */
@Api(tags = " 用户接口 ")
@RestController
@RequestMapping("/api/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    @Autowired
    private ISysUserService iSysUserService;

    @ApiOperation(value = "列表分页",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "limit",value = "每页数量",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "username",value = "用户名",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "nickname",value = "姓名",paramType = "query",dataType = "String"),

    })
    @GetMapping("list")
    private Result list(Integer page, Integer limit, String username, String nickname,String mobile,Integer status,Long deptId){
        SysUser user = new SysUser();
        user.setNickname(nickname);
        user.setMobile(mobile);
        user.setStatus(status);
        user.setDeptId(deptId);

        //iSysUserService.list();
       // Result.success(list)
        return null;
    }

    @ApiOperation(value = "更具用户名获取用户信息")
    @ApiImplicitParam(name = "username",value = "用户名",required = true,paramType = "path",dataType = "String")
    @GetMapping("username/{username}")
    public Result<SysUser> getUserByUsername(@PathVariable String username){

       SysUser user = iSysUserService.getByUsername(username);

        return Result.success(user);
    }

}
