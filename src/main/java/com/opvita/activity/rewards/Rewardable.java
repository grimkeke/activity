package com.opvita.activity.rewards;

import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import com.opvita.activity.model.EsOrderInfoBean;

/**
 * Created by rd on 2015/4/27.
 */
public interface Rewardable {

    // 对订单bean执行奖励reward，奖励对应的规则为rule,成功返回true，失败返回false
    public boolean executeReward(EsOrderInfoBean bean, Rule rule, RuleReward reward);

}
