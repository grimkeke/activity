package com.opvita.activity.utils;

import com.opvita.activity.model.HavePriority;

import java.math.BigDecimal;

/**
 * Created by rd on 2015/5/26.
 */
public class PriorityComparator {

    // 按优先级降序排列
    public static int descentOrder(HavePriority o1, HavePriority o2) {
        BigDecimal p1 = o1.getPriority();
        BigDecimal p2 = o2.getPriority();

        if (p1 == null) {
            return p2 == null ? 0 : -1;
        }

        if (p2 == null) {
            return 1;
        }

        return p2.subtract(p1).intValue();
    }
}
