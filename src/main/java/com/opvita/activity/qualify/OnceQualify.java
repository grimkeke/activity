package com.opvita.activity.qualify;

import com.opvita.activity.model.Rule;
import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTOWithBLOBs;
import com.opvita.activity.model.EsOrderInfoBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rd on 2015/5/31.
 * 计算当次资格
 */
@Component("onceQualify")
@Scope("prototype")
public class OnceQualify extends AbsQualify {

    @Override
    public BigDecimal calculateQualifyValue(EsOrderInfoBean bean, Rule rule) {
        EsOrderDTO esOrder = bean.getEsOrderDTO();
        List<EsOrderItemsDTOWithBLOBs> orderItemList = bean.getEsOrderItemsList();

        BigDecimal orderValue = new BigDecimal(0);
        if (rule.supportAllProducts()) {
            orderValue = getCalculator().qualifyOrderValue(esOrder);
            log.debug("order value:" + orderValue + " for order:" + esOrder.getSn());

        } else {
            for (EsOrderItemsDTOWithBLOBs orderItem : orderItemList) {
                String productId = String.valueOf(orderItem.getProductId());
                if (rule.productParticipate(productId)) {
                    BigDecimal itemValue = getCalculator().qualifyOrderItemValue(orderItem);
                    orderValue = orderValue.add(itemValue);
                }
            }
            log.debug("order value:" + orderValue + " for order:" + esOrder.getSn() + " set:" + rule.getProductSet());
        }
        return orderValue;
    }
}
