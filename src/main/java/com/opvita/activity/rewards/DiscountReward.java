package com.opvita.activity.rewards;

import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.model.RuleReward;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by rd on 2015/4/27.
 * 数值折扣
 */
@Service("discountReward")
public class DiscountReward extends AbsDiscountReward {

    @Override
    BigDecimal getDiscountResult(EsOrderDTO esOrder, RuleReward reward) {
        BigDecimal rewardValue = new BigDecimal(reward.getRewardValue());

        // 当前支付金额
        BigDecimal currentPayMoney = esOrder.getPaymoney();

        BigDecimal shouldPayMoney = currentPayMoney.subtract(rewardValue);
        return shouldPayMoney;
    }
}
