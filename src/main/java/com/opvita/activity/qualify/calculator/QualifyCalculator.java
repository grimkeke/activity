package com.opvita.activity.qualify.calculator;

import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTO;

import java.math.BigDecimal;

/**
 * Created by rd on 2015/5/31.
 * 资格计算器接口,目前有如下三种实现：按面额计算，按支付金额计算，按购买数量计算
 */
public interface QualifyCalculator {

    // 根据订单直接计算资格值
    public BigDecimal qualifyOrderValue(EsOrderDTO esOrder);

    // 根据订单项计算单个详情（即商品）的资格值
    public BigDecimal qualifyOrderItemValue(EsOrderItemsDTO esOrderItem);
}
