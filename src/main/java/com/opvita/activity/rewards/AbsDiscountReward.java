package com.opvita.activity.rewards;

import com.opvita.activity.daowrapper.EsOrderDAO;
import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.model.EsOrderInfoBean;
import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by rd on 2015/5/15.
 */
public abstract class AbsDiscountReward extends AbsReward {
    @Autowired private EsOrderDAO esOrderDAO;

    abstract BigDecimal getDiscountResult(EsOrderDTO esOrder, RuleReward reward);

    @Override
    boolean doReward(EsOrderInfoBean bean, Rule rule, RuleReward reward) {
        EsOrderDTO esOrder = bean.getEsOrderDTO();

        // 当前支付金额
        BigDecimal currentPayMoney = esOrder.getPaymoney();

        // 折扣后的金额,最少支付金额不能少于1分
        BigDecimal shouldPayMoney = getDiscountResult(esOrder, reward);
        if (shouldPayMoney.compareTo(new BigDecimal(0)) <= 0) {
            shouldPayMoney = new BigDecimal(1);
        }
        esOrder.setPaymoney(shouldPayMoney);

        log.info(String.format("order:%s satisfy reward:%s due to %s %s, pay money from %s to %s",
                esOrder.getSn(), reward.getId(), reward.getRewardType(), reward.getRewardValue().toString(),
                currentPayMoney.toString(), shouldPayMoney.toString()));

        esOrderDAO.updateOrder(esOrder);

        return true;
    }
}
