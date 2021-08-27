package com.core.common.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  基础分页请求对象
 *
 * @author:GSHG
 * @date: 2021-08-27 15:55
 * description:
 */
@Data
public class BasePageQuery {

    @ApiModelProperty(value = "当前页", example = "1")
    private int pageNum = 1;

    @ApiModelProperty(value = "每页记录数", example = "10")
    private int pageSize = 10;

}
