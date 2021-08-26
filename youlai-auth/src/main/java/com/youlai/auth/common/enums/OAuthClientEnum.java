package com.youlai.auth.common.enums;

import lombok.Getter;

/**
 * @author:GSHG
 * @date: 2021-08-26 12:13
 * description:
 */
public enum  OAuthClientEnum {
    /**
     *  授权key
     */
    TEST("client", "测试客户端"),
    ADMIN("youlai-admin", "系统管理端"),
    WEAPP("youlai-weapp", "微信小程序端");

    @Getter
    private String clientId;

    @Getter
    private String  desc;

    OAuthClientEnum(String clientId,String desc){
        this.clientId=clientId;
        this.desc=desc;
    }

    public static OAuthClientEnum getByClientId(String clientId){
        for(OAuthClientEnum clientEnum: OAuthClientEnum.values()){
            if(clientEnum.getClientId().equals(clientId)){
                return clientEnum;
            }
        }
        return null;
    }


}
