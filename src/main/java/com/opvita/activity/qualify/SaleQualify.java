package com.opvita.activity.qualify;

import com.opvita.activity.model.Rule;
import com.opvita.activity.model.SaleProductInfo;
import com.opvita.activity.daowrapper.SaleProductDAO;
import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTOWithBLOBs;
import com.opvita.activity.model.EsOrderInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rd on 2015/5/31.
 * 计算换购资格
 */
@Component("saleQualify")
@Scope("prototype")
public class SaleQualify extends AbsQualify {
    @Autowired private SaleProductDAO saleProductDAO;

    @Override
    public BigDecimal calculateQualifyValue(EsOrderInfoBean bean, Rule rule) {
        EsOrderDTO esOrder = bean.getEsOrderDTO();
        List<EsOrderItemsDTOWithBLOBs> orderItemList = bean.getEsOrderItemsList();
        SaleProductInfo info = saleProductDAO.getSaleProductInfo(esOrder.getIssuerId(), rule.getId());
        log.debug(info);

        BigDecimal orderValue = new BigDecimal(0);
        if (rule.supportAllProducts()) {
            for (EsOrderItemsDTOWithBLOBs orderItem : orderItemList) {
                String productId = String.valueOf(orderItem.getProductId());
                if (info.isNotSaleProduct(productId)) {
                    // 在所有商品均参与活动的情况下，只有非换购商品才参与计算资格
                    BigDecimal itemValue = getCalculator().qualifyOrderItemValue(orderItem);
                    orderValue = orderValue.add(itemValue);
                }
            }
            log.debug("order value:" + orderValue + " for order:" + esOrder.getSn());

        } else {
            for (EsOrderItemsDTOWithBLOBs orderItem : orderItemList) {
                String productId = String.valueOf(orderItem.getProductId());
                if (rule.productParticipate(productId) &&
                        info.isNotSaleProduct(productId)) {
                    // 如果不是所有商品都参与活动，计算资格的条件首先是商品参与活动，第二是商品不属于换购商品
                    BigDecimal itemValue = getCalculator().qualifyOrderItemValue(orderItem);
                    orderValue = orderValue.add(itemValue);
                }
            }
            log.debug("order value:" + orderValue + " for order:" + esOrder.getSn() + " set:" + rule.getProductSet());
        }

        return orderValue;
    }
}
