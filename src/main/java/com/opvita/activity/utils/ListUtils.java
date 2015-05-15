package com.opvita.activity.utils;

import java.util.List;

/**
 * Created by rd on 2015/4/28.
 */
public class ListUtils {
    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() < 1;
    }

    public static <T> boolean isNotEmpty(List<T> list) {
        return list != null && list.size() > 0;
    }
}
