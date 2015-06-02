package com.opvita.activity.qualify.calculator;

import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by rd on 2015/5/31.
 * 按购买数量计算资格，单位为张数
 */
@Component("countCalculator")
public class CountCalculator implements QualifyCalculator {
    @Override
    public BigDecimal qualifyOrderValue(EsOrderDTO esOrder) {
        // 返回总购买张数
        return new BigDecimal(esOrder.getGoodsNum());
    }

    @Override
    public BigDecimal qualifyOrderItemValue(EsOrderItemsDTO esOrderItem) {
        // 返回该商品的购买张数
        return new BigDecimal(esOrderItem.getNum());
    }
}
