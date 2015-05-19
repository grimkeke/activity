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

    // 删除该活动下的所有规则参与
    public int clearRuleParticipation(String activityId);

    // 删除该活动下的此规则
    public int removeRuleParticipation(String activityId, String ruleId);
}
