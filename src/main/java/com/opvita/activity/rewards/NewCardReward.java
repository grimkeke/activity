package com.opvita.activity.rewards;

import com.opvita.activity.model.RuleReward;
import com.opvita.activity.model.EsOrderInfoBean;
import org.springframework.stereotype.Service;

/**
 * Created by rd on 2015/4/27.
 * 奖励新卡
 */
@Service("newCardReward")
public class NewCardReward extends AbsNewCardReward {

    @Override
    String getRewardProductId(EsOrderInfoBean bean, RuleReward reward) {
        // 新卡奖励，返回指定的奖励商品id
        return reward.getRewardProduct();
    }
}
