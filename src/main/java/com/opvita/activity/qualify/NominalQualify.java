package com.opvita.activity.qualify;


import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTO;
import com.opvita.activity.dto.MProductInfoDTO;
import com.opvita.activity.utils.SysPara;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by rd on 2015/4/27.
 * 根据卡面金额计算的资格类型
 * 按分进行计算
 */
@Component("nominalQualify")
public class NominalQualify implements Qualify {
    private static Log log = LogFactory.getLog(NominalQualify.class);

    @Override
    public BigDecimal qualifyOrderValue(EsOrderDTO esOrder) {
        BigDecimal orderValue = esOrder.getGoodsAmount();
        log.debug("order value:" + orderValue + " for order:" + esOrder.getSn());
        return orderValue;
    }

    @Override
    public BigDecimal qualifyOrderItemValue(EsOrderItemsDTO esOrderItem) {
        String productId = String.valueOf(esOrderItem.getProductId());
        BigDecimal count = new BigDecimal(esOrderItem.getNum());

        // 当订单商品属于参与规则时，因为es_order_item没有记录商品卡面金额，所以需要根据商品号查询
        MProductInfoDTO productInfo = SysPara.getMProductInfoDTOByProductId(productId);

        BigDecimal itemValue = count.multiply(new BigDecimal(productInfo.getCardPrice()));

        log.debug("item value:" + itemValue + " for esOrderItem:" + esOrderItem.getItemId());
        return itemValue;
    }
}
