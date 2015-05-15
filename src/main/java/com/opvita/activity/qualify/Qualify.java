package com.opvita.activity.qualify;

import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTO;

import java.math.BigDecimal;

/**
 * Created by rd on 2015/4/27.
 */
public interface Qualify {
    public static final String NOMINAL = "NOMINAL";
    public static final String PAYMENT = "PAYMENT";

    // 计算整个订单的资格值
    public BigDecimal qualifyOrderValue(EsOrderDTO esOrder);

    // 计算单个订单详情（即商品）的资格值
    public BigDecimal qualifyOrderItemValue(EsOrderItemsDTO esOrderItem);

}
