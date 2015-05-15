package com.opvita.activity.daowrapper;

import com.opvita.activity.model.RuleReward;

import java.util.List;

/**
 * Created by rd on 2015/4/25.
 */
public interface RuleRewardDAO {
    // 保存规则数据
    public RuleReward saveReward(RuleReward data);

    // 根据规则id获取规则数据
    public List<RuleReward> getReward(String ruleId);

    // 根据规则删除所有规则数据
    public int removeReward(String ruleId);

    // 更新当前奖励次数
    public RuleReward increaseCurrentReward(RuleReward reward);
}
