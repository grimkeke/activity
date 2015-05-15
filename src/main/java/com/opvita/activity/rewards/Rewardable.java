package com.opvita.activity.rewards;

import com.opvita.activity.model.EsOrderInfoBean;
import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;

/**
 * Created by rd on 2015/4/27.
 */
public interface Rewardable {
    public static final String NEW_CARD = "NEW_CARD";          // 新卡奖励
    public static final String DISCOUNT = "DISCOUNT";          // 数值折扣
    public static final String PERCENTAGE = "PERCENTAGE";      // 百分比折扣

    // 对订单bean执行奖励reward，奖励对应的规则为rule,成功返回true，失败返回false
    public boolean executeReward(EsOrderInfoBean bean, Rule rule, RuleReward reward);

}
