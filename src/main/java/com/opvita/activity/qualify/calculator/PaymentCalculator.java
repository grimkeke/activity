package com.opvita.activity.qualify.calculator;

import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by rd on 2015/5/31.
 * 按支付金额计算资格，单位为分
 */
@Component("paymentCalculator")
public class PaymentCalculator implements QualifyCalculator {
    @Override
    public BigDecimal qualifyOrderValue(EsOrderDTO esOrder) {
        // 返回所有商品支付金额
        return esOrder.getOrderAmount();
    }

    @Override
    public BigDecimal qualifyOrderItemValue(EsOrderItemsDTO esOrderItem) {
        BigDecimal count = new BigDecimal(esOrderItem.getNum());

        BigDecimal itemValue = count.multiply(esOrderItem.getPrice());
        return itemValue;
    }
}
