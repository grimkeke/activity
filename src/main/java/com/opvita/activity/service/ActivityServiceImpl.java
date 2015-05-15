package com.opvita.activity.service;

import com.opvita.activity.enums.RewardSituation;
import com.opvita.activity.model.*;
import com.opvita.activity.utils.ListUtils;
import com.opvita.activity.daowrapper.ActivityDAO;
import com.opvita.activity.daowrapper.RewardLogDAO;
import com.opvita.activity.daowrapper.RuleRewardDAO;
import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.model.EsOrderInfoBean;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rd on 2015/4/27.
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    private static Log log = LogFactory.getLog(ActivityServiceImpl.class);

    @Autowired
    private ActivityDAO activityDAO;
    @Autowired
    private RewardLogDAO rewardLogDAO;
    @Autowired
    private RuleRewardDAO ruleRewardDAO;

    @Override
    public void executeActivity(EsOrderInfoBean bean, RewardSituation situation) {
        List<Activity> satisfiedActivityList = getSatisfiedActivities(bean, situation);
        if (ListUtils.isNotEmpty(satisfiedActivityList)) {
            for (Activity satisfiedActivity : satisfiedActivityList) {

                List<Rule> satisfiedRuleList = satisfiedActivity.getRules();
                if (ListUtils.isNotEmpty(satisfiedRuleList)) {
                    for (Rule satisfiedRule : satisfiedRuleList) {

                        List<RuleReward> rewardList = satisfiedRule.getRewardList();
                        if (ListUtils.isNotEmpty(rewardList)) {
                            for (RuleReward reward : rewardList) {
                                log.debug("satisfied reward:" + reward);

                                // 查询该用户已奖励多少次
                                int rewardCount = rewardLogDAO.getRewardCount(bean, reward);

                                // 奖励次数未超限才能进行奖励
                                if (!reward.exceedLimit(rewardCount)) {
                                    // 记录奖励日志，初始状态为待奖励
                                    RewardLog rewardLog = rewardLogDAO.saveRewardLog(bean, reward);
                                    log.info("log reward start. " + rewardLog);

                                    // 更新当前奖励数据
                                    ruleRewardDAO.increaseCurrentReward(reward);

                                    // 执行奖励
                                    if (reward.executeReward(satisfiedRule, bean)) {
                                        // 奖励执行成功后更新奖励日志
                                        rewardLogDAO.completeRewardLog(rewardLog);
                                        log.info("log reward complete. " + rewardLog.getId());
                                    }
                                }
                            }
                        }

                        // 规则互斥
                        if (satisfiedRule.isMutex()) {
                            log.info("stop reward at rule:" + satisfiedRule);
                            break;
                        }
                    }
                }

                // 活动互斥
                if (satisfiedActivity.isMutex()) {
                    log.info("stop reward at activity:" + satisfiedActivity);
                    break;
                }
            }
        }
    }

    @Override
    public List<Activity> getSatisfiedActivities(EsOrderInfoBean bean, RewardSituation situation) {
        List<Activity> satisfiedActivityList = new ArrayList<Activity>();

        EsOrderDTO esOrder = bean.getEsOrderDTO();
        String issuerId = esOrder.getIssuerId();

        List<Activity> activityList = activityDAO.getActivities(issuerId, situation);
        if (ListUtils.isNotEmpty(activityList)) {
            Collections.sort(activityList, new Activity());

            for (Activity activity : activityList) {
                List<Rule> satisfiedRuleList = activity.getSatisfiedRules(bean);

                if (ListUtils.isNotEmpty(satisfiedRuleList)) {
                    activity.setRuleList(satisfiedRuleList);
                    satisfiedActivityList.add(activity);
                }
            }
        }
        return satisfiedActivityList;
    }

    @Override
    public void executeRules(List<String> ruleIdList, EsOrderInfoBean bean) {
        // todo implement
        throw new NotImplementedException("not implement yet!");
    }
}
