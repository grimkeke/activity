package com.opvita.activity.daowrapper;

import com.opvita.activity.model.RewardLog;
import com.opvita.activity.model.RuleReward;
import com.opvita.activity.model.EsOrderInfoBean;

import java.util.List;

/**
 * Created by rd on 2015/5/14.
 */
public interface RewardLogDAO {
    // 保存订单bean的奖励数据
    public RewardLog saveRewardLog(EsOrderInfoBean bean, RuleReward reward);

    // (use internal) 通过订单bean查询订单对应用户享受奖励的信息
    @Deprecated
    public List<RewardLog> getRewardLogs(EsOrderInfoBean bean, RuleReward reward);

    // 通过订单bean查询订单对应用户享受奖励的次数
    public int getRewardCount(EsOrderInfoBean bean, RuleReward reward);

    // 更新奖励记录为已完成
    public RewardLog completeRewardLog(RewardLog rewardLog);
}
