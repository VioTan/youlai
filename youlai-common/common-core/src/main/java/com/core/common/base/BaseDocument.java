package com.core.common.base;

import lombok.Data;

/**
 * ocument 是 ES 里的一个 JSON 对象，包括零个或多个field，类比关系数据库的一行记录
 * @author:GSHG
 * @date: 2021-08-27 15:51
 * description:
 */
@Data
public class BaseDocument {

    /**
     *  数据唯一标识
     */
    private String id;

    /**
     *  索引名称
     */
    private String index;


}
