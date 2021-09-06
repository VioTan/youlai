package com.youlai.admin.controller;

import com.core.common.result.Result;
import com.youlai.admin.api.pojo.entity.SysOauthClient;
import com.youlai.admin.service.ISysOauthClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:GSHG
 * @date: 2021-08-26 14:57
 * description:
 */

@Api(tags = "客户端接口")
@RestController
@RequestMapping("/api/v1/oauth-clients")
@AllArgsConstructor
public class OauthClientController {

    private ISysOauthClientService iSysOauthClientService;

    @ApiOperation(value = "客户端详情")
    @ApiImplicitParam(name = "clientId",value = "客户端id",required = true,paramType = "path",dataType = "String")
    @GetMapping("/{clientId}")
    public Result details(@PathVariable String clientId){
        SysOauthClient client = iSysOauthClientService.getById(clientId);
        return Result.success(client);
    }

}
