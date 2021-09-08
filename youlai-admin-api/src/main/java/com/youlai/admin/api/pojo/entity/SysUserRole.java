package com.youlai.admin.api.pojo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:GSHG
 * @date: 2021-09-08 12:04
 * description:
 */
@Data
@Accessors(chain = true)
public class SysUserRole {

    private Long userId;

    private Long roleId;

}
