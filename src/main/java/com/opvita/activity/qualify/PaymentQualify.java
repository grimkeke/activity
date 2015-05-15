package com.opvita.activity.qualify;

import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by rd on 2015/4/27.
 * 根据支付金额计算的资格类型
 * 按分进行计算
 */
@Component("paymentQualify")
public class PaymentQualify implements Qualify {
    private static Log log = LogFactory.getLog(PaymentQualify.class);

    @Override
    public BigDecimal qualifyOrderValue(EsOrderDTO esOrder) {
        BigDecimal orderValue = esOrder.getOrderAmount();
        log.debug("order value:" + orderValue + " for order:" + esOrder.getSn());
        return orderValue;
    }

    @Override
    public BigDecimal qualifyOrderItemValue(EsOrderItemsDTO esOrderItem) {
        BigDecimal count = new BigDecimal(esOrderItem.getNum());

        BigDecimal itemValue = count.multiply(esOrderItem.getPrice());

        log.debug("item value:" + itemValue + " for esOrderItem:" + esOrderItem.getItemId());
        return itemValue;
    }
}
