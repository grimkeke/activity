package com.opvita.activity.daowrapper;


import com.opvita.activity.dto.MRuleParticipationDTO;

import java.util.List;

/**
 * Created by rd on 2015/4/27.
 */
public interface RuleParticipationDAO {
    // 向活动添加一条规则
    public MRuleParticipationDTO saveRuleParticipation(String activityId, String ruleId);

    // 获取一个活动下的所有规则
    public List<MRuleParticipationDTO> getRuleParticipation(String activityId);

    // 获取一个规则参与了哪些活动
    public List<MRuleParticipationDTO> getActivityParticipation(String ruleId);

    // 判断活动与规则是否匹配
    public boolean isValidActivityAndRule(String activityId, String ruleId);

    // 物理删除该活动下的所有规则参与
    // 推荐使用软删除
    public int clearRuleParticipation(String activityId);

    // 软删除该活动下的所有规则参与
    public int invalidateRuleParticipation(String activityId);

    // 物理删除该活动下的此规则
    // 推荐使用软删除
    public int removeRuleParticipation(String activityId, String ruleId);

    // 软删除该活动下的此规则
    public int invalidateRuleParticipation(String activityId, String ruleId);
}
