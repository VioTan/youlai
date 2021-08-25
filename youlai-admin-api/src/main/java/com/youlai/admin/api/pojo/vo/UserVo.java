package com.youlai.admin.api.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author:GSHG
 * @date: 2021-08-25 12:01
 * description:
 */
@Data
public class UserVo {

    private Long id;

    private String nickname;

    private String avatar;

    private List<String> roles;

    private List<String> perms ;
}
