package com.youlai.auth.common.enums;

import lombok.Getter;

public enum PasswordEncoderTypeEnum {
    /**
     * 明文类型
     */
    BCRYPT("{bcrypt}","BCRYPT加密"),
    NOOP("{noop}","无加密明文");

    @Getter
    private String prefx;

    PasswordEncoderTypeEnum(String prefix,String desc){
        this.prefx = prefix;
    }

}
