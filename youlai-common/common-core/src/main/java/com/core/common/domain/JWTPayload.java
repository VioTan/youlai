package com.core.common.domain;

import lombok.Data;

/**
 * @author:GSHG
 * @date: 2021-08-27 16:08
 * description:
 */
@Data
public class JWTPayload {

    private String jti;

    private Long exp;

}
