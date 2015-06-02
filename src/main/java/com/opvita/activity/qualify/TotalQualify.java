package com.opvita.activity.qualify;

import com.opvita.activity.model.Activity;
import com.opvita.activity.model.Rule;
import com.opvita.activity.utils.ListUtils;
import com.opvita.activity.daowrapper.ActivityDAO;
import com.opvita.activity.daowrapper.EsOrderDAO;
import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTO;
import com.opvita.activity.model.EsOrderInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by rd on 2015/5/30.
 * 计算累积资格
 */
@Component("totalQualify")
@Scope("prototype")
public class TotalQualify extends OnceQualify {
    @Autowired private ActivityDAO activityDAO;
    @Autowired private EsOrderDAO esOrderDAO;

    @Override
    public BigDecimal calculateQualifyValue(EsOrderInfoBean bean, Rule rule) {
        EsOrderDTO esOrder = bean.getEsOrderDTO();
        // 首先计算当前订单资格，然后查询流水，计算活动期间的累积资格
        BigDecimal total = super.calculateQualifyValue(bean, rule);
        log.info("current value:" + total);

        Activity activity = activityDAO.getActivity(rule.getActivityId());
        Date start = activity.getValidStart();
        Date end = activity.getValidEnd();

        List<EsOrderDTO> orderList = esOrderDAO.getEsOrderList(esOrder.getCustSeq(), esOrder.getIssuerId(), start, end);
        log.info("history order list:" + orderList);
        if (ListUtils.isNotEmpty(orderList)) {
            for (EsOrderDTO order : orderList) {
                if (rule.supportAllProducts()) {
                    // 如果支持所有商品，简单返回订单总金额
                    total = total.add(getCalculator().qualifyOrderValue(order));

                } else {
                    List<EsOrderItemsDTO> itemList = esOrderDAO.getOrderItemList(order.getOrderId());
                    if (ListUtils.isNotEmpty(itemList)) {
                        for (EsOrderItemsDTO item : itemList) {
                            String productId = String.valueOf(item.getProductId());
                            if (rule.productParticipate(productId)) {
                                // 如果只有有限商品参与规则，则只能累加参与活动的商品卡面金额
                                total = total.add(getCalculator().qualifyOrderItemValue(item));
                            }
                        }
                    }
                }
            }
        }

        log.info("final value:" + total);
        return total;
    }
}
