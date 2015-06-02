package com.opvita.activity.rewards;

import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import com.opvita.activity.model.SaleProduct;
import com.opvita.activity.model.SaleProductInfo;
import com.opvita.activity.daowrapper.SaleProductDAO;
import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTOWithBLOBs;
import com.opvita.activity.model.EsOrderInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rd on 2015/5/30.
 * 满额换购奖励
 * 换购奖励必须和换购资格一起使用
 * @see com.opvita.activity.qualify.SaleQualify
 */
@Component("saleReward")
public class SaleReward extends AbsDiscountReward {
    @Autowired private SaleProductDAO saleProductDAO;

    @Override
    BigDecimal getDiscountResult(EsOrderInfoBean bean, Rule rule, RuleReward reward) {
        EsOrderDTO esOrder = bean.getEsOrderDTO();
        List<EsOrderItemsDTOWithBLOBs> orderItemList = bean.getEsOrderItemsList();

        if (rule.checkOrder(bean) == null) {
            // 在执行换购奖励之前再次验证换购资格。double check
            log.warn("order:" + esOrder.getSn() + " is not satisfied " + rule + " rewardId:" + reward.getId());
            return esOrder.getPaymoney();
        }

        // 查询换购商品列表，如果订单中包含该商品，则按换购商品价格更新订单支付价格,包含换购商品的换购价格
        SaleProductInfo info = saleProductDAO.getSaleProductInfo(esOrder.getIssuerId(), rule.getId());
        log.info(info);

        // 根据订单和换购商品信息更新应支付金额
        BigDecimal shouldPayMoney = esOrder.getPaymoney();
        for (EsOrderItemsDTOWithBLOBs orderItem : orderItemList) {
            String productId = String.valueOf(orderItem.getProductId());
            if (info.isSaleProduct(productId)) {
                // 获取所有换购商品的价格，分别更新订单表和详情表（记录换购价格）
                SaleProduct saleProduct = info.getSaleProduct(productId);
                Long salePrice = saleProduct.getSalePrice();

                // 使用换购价更新商品支付金额, 即先加上换购价，然后减去原价
                shouldPayMoney = shouldPayMoney.add(new BigDecimal(salePrice)).subtract(orderItem.getPrice());
                log.info("shouldPay:" + shouldPayMoney + " productId:" + productId +
                            " item price:" + orderItem.getPrice() + " sale price:" + salePrice);

                // 设置换购价格至订单详情, 把商品原始支付金额保存至gainedPoint字段
                orderItem.setGainedpoint(orderItem.getPrice().longValue());
                orderItem.setPrice(new BigDecimal(salePrice));
                esOrderDAO.updateOrderItem(orderItem);
            }
        }
        return shouldPayMoney;
    }
}
