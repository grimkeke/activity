package com.opvita.activity.service.impl;

import com.opvita.activity.enums.RewardSituation;
import com.opvita.activity.model.Activity;
import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import com.opvita.activity.service.ActivityService;
import com.opvita.activity.utils.ListUtils;
import com.opvita.activity.daowrapper.ActivityDAO;
import com.opvita.activity.daowrapper.ActivityRuleDAO;
import com.opvita.activity.daowrapper.RuleParticipationDAO;
import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.MRuleParticipationDTO;
import com.opvita.activity.model.EsOrderInfoBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by rd on 2015/4/27.
 */
@Service("localActivityService")
public class ActivityServiceImpl implements ActivityService {
    private static Log log = LogFactory.getLog(ActivityServiceImpl.class);

    @Autowired private ActivityDAO activityDAO;
    @Autowired private ActivityRuleDAO ruleDAO;
    @Autowired private RuleParticipationDAO ruleParticipationDAO;

    @Override
    public void executeActivity(EsOrderInfoBean bean, RewardSituation situation) {
        List<Activity> satisfiedActivityList = getSatisfiedActivities(bean, situation);
        executeActivityList(bean, satisfiedActivityList);
    }

    private void executeActivityList(EsOrderInfoBean bean, List<Activity> activityList) {
        if (ListUtils.isNotEmpty(activityList)) {
            for (Activity satisfiedActivity : activityList) {

                List<Rule> satisfiedRuleList = satisfiedActivity.getRuleList();
                executeRuleList(bean, satisfiedRuleList);

                // 活动互斥
                if (satisfiedActivity.isMutex()) {
                    log.info("stop reward at activity:" + satisfiedActivity.getId());
                    break;
                }
            }
        }
    }

    private void executeRuleList(EsOrderInfoBean bean, List<Rule> ruleList) {
        if (ListUtils.isNotEmpty(ruleList)) {
            for (Rule satisfiedRule : ruleList) {

                List<RuleReward> rewardList = satisfiedRule.getRewardList();
                if (ListUtils.isNotEmpty(rewardList)) {
                    for (RuleReward reward : rewardList) {
                        log.debug("may satisfy reward:" + reward.getId());
                        reward.executeReward(satisfiedRule, bean);
                    }
                }

                // 规则互斥
                if (satisfiedRule.isMutex()) {
                    log.info("stop reward at rule:" + satisfiedRule.getId());
                    break;
                }
            }
        }
    }

    @Override
    public List<Activity> getSatisfiedActivities(EsOrderInfoBean bean, RewardSituation situation) {
        EsOrderDTO esOrder = bean.getEsOrderDTO();
        String issuerId = esOrder.getIssuerId();

        List<Activity> activityList = activityDAO.getActivities(issuerId, situation);
        return filterSatisfiedActivities(bean, activityList);
    }

    // 筛选出订单满足的活动
    private List<Activity> filterSatisfiedActivities(EsOrderInfoBean bean, List<Activity> activityList) {
        List<Activity> satisfiedActivityList = new ArrayList<Activity>();

        if (ListUtils.isNotEmpty(activityList)) {
            for (Activity activity : activityList) {
                List<Rule> satisfiedRuleList = activity.getSatisfiedRules(bean);

                if (ListUtils.isNotEmpty(satisfiedRuleList)) {
                    activity.setRuleList(satisfiedRuleList);
                    satisfiedActivityList.add(activity);
                }
            }
        }
        log.debug("satisfied activities:" + satisfiedActivityList);
        return satisfiedActivityList;
    }

    @Override
    public void executeRules(EsOrderInfoBean bean, List<String> ruleIds) {
        List<Rule> ruleList = ruleDAO.getRules(ruleIds);
        List<Activity> activityList = arrangeActivityList(ruleList);
        List<Activity> satisfiedActivityList = filterSatisfiedActivities(bean, activityList);
        executeActivityList(bean, satisfiedActivityList);
    }

    // 通过rule列表反向组成activityList
    private List<Activity> arrangeActivityList(List<Rule> ruleList) {
        if (ListUtils.isEmpty(ruleList)) {
            return null;
        }

        Map<String, Activity> activityMap = new HashMap<String, Activity>();
        for (Rule rule : ruleList) {
            List<MRuleParticipationDTO> participationList = ruleParticipationDAO.getActivityParticipation(rule.getId());
            if (ListUtils.isNotEmpty(participationList)) {
                for (MRuleParticipationDTO participation : participationList) {
                    String activityId = participation.getActivityId();

                    Activity activity = activityMap.get(activityId);
                    if (activity == null) {
                        activity = activityDAO.getActivity(activityId, false);
                        activityMap.put(activityId, activity);
                    }
                    activity.addRule(rule);
                }
            }
        }

        List<Activity> activityList = new ArrayList<Activity>();
        for (String key : activityMap.keySet()) {
            activityList.add(activityMap.get(key));
        }
        Collections.sort(activityList, Activity.INSTANCE);
        return activityList;
    }
}
