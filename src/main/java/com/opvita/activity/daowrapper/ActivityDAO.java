package com.opvita.activity.daowrapper;

import com.opvita.activity.enums.RewardSituation;
import com.opvita.activity.model.Activity;
import com.opvita.activity.model.Rule;

import java.util.List;

/**
 * Created by rd on 2015/4/25.
 */
public interface ActivityDAO {
    // 根据活动id（主键）获取活动信息
    public Activity getActivity(String activityId);
    public Activity getActivity(String activityId, boolean withRules);

    // (仅测试和类内部使用)获取一个商户下的活动列表（包含活动参与规则）已按优先级倒序排序
    @Deprecated
    public List<Activity> getActivities(String issuerId);

    // 根据奖励发生的情况，获取一个商户下的活动列表（包含活动参与规则）已按优先级倒序排序
    public List<Activity> getActivities(String issuerId, RewardSituation situation);

    // 保存一个活动
    public Activity saveActivityOnly(Activity activity);

    // 保存一个活动，并且保存对应规则
    public Activity saveActivity(Activity activity, List<Rule> ruleList);

    // 向活动添加规则
    public Activity attachRule(String activityId, Rule rule);
    public Activity attachRules(String activityId, List<Rule> ruleList);

    // 根据活动id物理删除该活动，即删除活动、规则、规则参与、商品参与和规则数据
    // 推荐使用软删除
    public void removeActivity(String activityId);

    // 根据活动id软删除，操作表同removeActivity方法
    public void invalidateActivity(String activityId);

    // 使所有商户的活动缓存失效
    public void syncActivities();

    // 使issuerId对应商户的活动缓存失效
    public void syncActivities(String issuerId);
}
