package com.opvita.activity.daowrapper;

import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;

import java.util.List;

/**
 * Created by rd on 2015/4/25.
 */
public interface ActivityRuleDAO {
    // 根据规则主键获取规则
    public Rule getRule(String ruleId);
    public Rule getRuleOnly(String ruleId);

    // 保存规则，并且保存规则数据
    public Rule saveRule(Rule rule);

    // 保存规则，并且保存规则数据
    public Rule saveRule(Rule rule, List<RuleReward> dataList);

    // 删除活动和规则的关联关系，同时删除规则，(调用removeRule)
    public void detachRule(String activityId, String ruleId);

    // 根据规则id删除规则，同时删除商品参与规则、规则数据
    public void removeRule(String ruleId);

}
