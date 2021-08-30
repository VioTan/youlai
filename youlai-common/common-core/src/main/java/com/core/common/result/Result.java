package com.core.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:GSHG
 * @date: 2021-08-16 14:15
 * description:
 */
@Data
public class Result<T> implements Serializable {

    private String code;

    private T data;

    private String msg;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        //获取执行成功得代码
        ResultCodeEnum rce = ResultCodeEnum.SUCCESS;
        if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
            //如果是false，则改成失败得代码
            rce = ResultCodeEnum.SYSTEM_EXECUTION_ERROR;
        }
        return result(rce, data);
    }
    public static <T> Result<T> error() {
        return result(ResultCodeEnum.SYSTEM_EXECUTION_ERROR.getCode(), ResultCodeEnum.SYSTEM_EXECUTION_ERROR.getMsg(), null);
    }

    public static <T> Result<T> error(String msg) {
        return result(ResultCodeEnum.SYSTEM_EXECUTION_ERROR.getCode(), msg, null);
    }

    public static <T> Result<T> status(boolean status) {
        if (status) {
            return success();
        } else {
            return error();
        }
    }

    public static <T> Result<T> custom(IResultCode resultCode) {
        return result(resultCode.getCode(), resultCode.getMsg(), null);
    }

    /**
     *
     * @param resultCode 枚举类
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Result<T> result(IResultCode resultCode, T data) {

        return result(resultCode.getCode(), resultCode.getMsg(), data);
    }

    /**
     * 根据枚举类的code,枚举类的msg,data，返回结果集
     * @param code
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Result<T> result(String code, String msg, T data) {

        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> failed(IResultCode resultCode) {
        return result(resultCode.getCode(), resultCode.getMsg(), null);
    }

}
