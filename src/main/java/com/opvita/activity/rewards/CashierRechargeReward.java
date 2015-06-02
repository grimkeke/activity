package com.opvita.activity.rewards;

import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.model.EsOrderInfoBean;
import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by rd on 2015/5/26.
 * 充值操作员主卡奖励
 */
@Service("cashierRechargeReward")
public class CashierRechargeReward extends AbsReward {

    @Override
    boolean doReward(EsOrderInfoBean esBean, Rule rule, RuleReward reward) {
        EsOrderDTO esOrder = esBean.getEsOrderDTO();

        // 充值金额,单位为分
        BigDecimal rewardValue = new BigDecimal(reward.getRewardValue());
        // 转换为元
        String rechargeValue = rewardValue.divide(new BigDecimal(100)).toString();
        String custSeq = esOrder.getCustSeq();

        log.info(String.format("order:%s satisfy reward:%s due to %s %s, system will recharge cashier:%s",
                esOrder.getSn(), reward.getId(), reward.getRewardType(), rewardValue,
                esOrder.getBuyerMobile() + "@" + custSeq));

        // todo
        return true;
    }
}
