package com.opvita.activity.service;

import com.opvita.activity.enums.RewardSituation;
import com.opvita.activity.model.Activity;
import com.opvita.activity.model.EsOrderInfoBean;

import java.util.List;

/**
 * Created by rd on 2015/4/25.
 */
public interface ActivityService {

    // 根据奖励情形（下单前还是支付成功后），对订单执行活动规则
    public void executeActivity(EsOrderInfoBean bean, RewardSituation situation);

    // 根据奖励情况（下单前还是支付成功后），对订单校验活动规则, 返回所有满足的活动和规则
    public List<Activity> getSatisfiedActivities(EsOrderInfoBean bean, RewardSituation situation);

    // 对于冲突的规则，由用户选择执行哪些规则（未实现）
    // 根据规则id对订单执行规则，如果同一活动中存在不可共享的规则，或者存在多个不可共享的活动，则返回错误
    public void executeRules(EsOrderInfoBean bean, List<String> ruleIds);
}
