package com.opvita.activity.qualify.calculator;

import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTO;
import com.opvita.activity.dto.MProductInfoDTO;
import com.opvita.activity.utils.SysPara;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by rd on 2015/5/31.
 * 按面额计算资格，单位为分
 */
@Component("nominalCalculator")
public class NominalCalculator implements QualifyCalculator {
    @Override
    public BigDecimal qualifyOrderValue(EsOrderDTO esOrder) {
        // 返回所有商品卡面金额
        return esOrder.getGoodsAmount();
    }

    @Override
    public BigDecimal qualifyOrderItemValue(EsOrderItemsDTO esOrderItem) {
        String productId = String.valueOf(esOrderItem.getProductId());
        BigDecimal count = new BigDecimal(esOrderItem.getNum());

        // 当订单商品属于参与规则时，因为es_order_item没有记录商品卡面金额，所以需要根据商品号查询
        MProductInfoDTO productInfo = SysPara.getMProductInfoDTOByProductId(productId);

        BigDecimal itemValue = count.multiply(new BigDecimal(productInfo.getCardPrice()));
        return itemValue;
    }
}
