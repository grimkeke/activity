package com.opvita.activity.rewards;

import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.model.EsOrderInfoBean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by rd on 2015/5/12.
 * 百分比折扣
 */
@Service("percentageReward")
public class PercentageReward extends AbsDiscountReward {

    @Override
    BigDecimal getDiscountResult(EsOrderInfoBean bean, Rule rule, RuleReward reward) {
        EsOrderDTO esOrder = bean.getEsOrderDTO();

        // 满额打折折扣，百分比
        BigDecimal rewardValue = new BigDecimal(reward.getRewardValue());

        // 当前支付金额
        BigDecimal currentPayMoney = esOrder.getPaymoney();

        // 按rewardValue进行百分比折扣
        BigDecimal shouldPayMoney = currentPayMoney.multiply(rewardValue).divide(new BigDecimal(100));
        return shouldPayMoney;
    }
}
