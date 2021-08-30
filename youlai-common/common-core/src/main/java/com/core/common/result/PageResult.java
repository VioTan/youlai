package com.core.common.result;

import lombok.Data;

/**
 * @author:GSHG
 * @date: 2021-08-16 14:14
 * description:
 */
@Data
public class PageResult<T> extends Result {

    private Long total;

    public static <T> PageResult<T> success(T data,Long total){
        PageResult<T> pageResult = new PageResult();
        pageResult.setCode(ResultCodeEnum.SUCCESS.getCode());
        pageResult.setMsg(ResultCodeEnum.SUCCESS.getMsg());
        pageResult.setData(data);
        pageResult.setTotal(total);
        return pageResult;
    }


}
