package com.opvita.activity.dao;

/**
 * Created by rd on 2015/4/26.
 */
public interface ActivityMapper {
    public String nextActivityId();
    public String nextActivityRuleId();
    public String nextRuleRewardId();
    public String nextRewardLogId();
    public String nextRuleParticipationId();
    public String nextProductParticipationId();
}
