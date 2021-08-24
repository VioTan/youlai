package com.core.common.util;

import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;

/**
 * @author:GSHG
 * @date: 2021-08-16 15:07
 * description:
 */
@NoArgsConstructor
public class CollectionUtils {

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
}
