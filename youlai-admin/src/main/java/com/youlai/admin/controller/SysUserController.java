package com.youlai.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youlai.admin.entity.SysUser;
import com.youlai.admin.service.ISysUserService;
import core.result.PageResult;
import core.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
public class SysUserController {
    @Autowired
    private ISysUserService iSysUserService;

    @ApiOperation(value = "列表分页",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "limit",value = "每页数量",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "username",value = "用户名",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "nickname",value = "姓名",paramType = "query",dataType = "String"),

    })
    @GetMapping("/list")
    private Result list(Integer page,Integer limit,String username,String nickname){
       LambdaQueryWrapper<SysUser> queryWrapper =new LambdaQueryWrapper<SysUser>()
               .like(StrUtil.isNotBlank(username),SysUser::getUsername,username)
               .like(StrUtil.isNotBlank(nickname),SysUser::getNickname,nickname)
               .orderByDesc(SysUser::getCreateBy)
               .orderByDesc(SysUser:: getGmtCreate
               );
       if(page !=null && limit !=null){
                Page<SysUser> result = iSysUserService.page(new Page<>(page,limit),queryWrapper);
                return PageResult.success(result.getRecords(),result.getTotal());
       }else if(limit != null){
           queryWrapper.last("LIMIT" + limit);

       }
        List<SysUser> list = iSysUserService.list(queryWrapper);
        return Result.success(list);
    }
}
