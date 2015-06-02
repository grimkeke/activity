package com.opvita.activity.rewards;

import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.model.EsOrderInfoBean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by rd on 2015/4/27.
 * 数值折扣
 */
@Service("discountReward")
public class DiscountReward extends AbsDiscountReward {

    @Override
    BigDecimal getDiscountResult(EsOrderInfoBean bean, Rule rule, RuleReward reward) {
        EsOrderDTO esOrder = bean.getEsOrderDTO();

        // 满减金额,单位为分
        BigDecimal rewardValue = new BigDecimal(reward.getRewardValue());

        // 当前支付金额
        BigDecimal currentPayMoney = esOrder.getPaymoney();

        // 按rewardValue进行减扣
        BigDecimal shouldPayMoney = currentPayMoney.subtract(rewardValue);
        return shouldPayMoney;
    }
}
