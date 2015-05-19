package com.opvita.activity.daowrapper.impl;

import com.opvita.activity.common.Constants;
import com.opvita.activity.dao.ActivityMapper;
import com.opvita.activity.dao.MActivityDTOMapper;
import com.opvita.activity.daowrapper.ActivityDAO;
import com.opvita.activity.daowrapper.ActivityRuleDAO;
import com.opvita.activity.daowrapper.RuleParticipationDAO;
import com.opvita.activity.dto.MActivityDTO;
import com.opvita.activity.dto.MActivityDTOCriteria;
import com.opvita.activity.dto.MRuleParticipationDTO;
import com.opvita.activity.enums.RewardSituation;
import com.opvita.activity.model.Activity;
import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import com.opvita.activity.utils.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rd on 2015/4/25.
 */
@Service
public class ActivityDAOImpl implements ActivityDAO {
    private static Log log = LogFactory.getLog(ActivityDAOImpl.class);

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private MActivityDTOMapper mapper;
    @Autowired
    private RuleParticipationDAO ruleParticipationDAO;
    @Autowired
    private ActivityRuleDAO ruleDAO;

    private static Map<String, List<Activity>> cache = new ConcurrentHashMap<String, List<Activity>>();
    private static Map<String, Boolean> cacheValidMap = new ConcurrentHashMap<String, Boolean>();

    private static boolean cacheValid(String issuerId) {
        boolean valid = cacheValidMap.get(issuerId) != null &&
                            cacheValidMap.get(issuerId) == true;
        log.debug("cache valid: " + valid + " cacheValidMap:" + cacheValidMap);
        return valid;
    }

    @Override
    public Activity getActivity(String activityId) {
        return getActivity(activityId, false);
    }

    @Override
    public Activity getActivity(String activityId, boolean withRules) {
        MActivityDTO dto = mapper.selectByPrimaryKey(activityId);
        Activity activity = Activity.fromDTO(dto);

        if (withRules && activity != null) {
            attachRules(activity);
        }
        return activity;
    }

    @Override
    public List<Activity> getActivities(String issuerId) {
        if (StringUtils.isEmpty(issuerId)) {
            throw new NullPointerException("issuserId should not be null!");
        }
        return getActivities(issuerId, null, null, null, null, Constants.ON);
    }

    @Override
    public List<Activity> getActivities(String issuerId, RewardSituation situation) {
        if (situation == null) {
            throw new NullPointerException(issuerId + " reward situation should not be empty!");
        }

        List<Activity> activityList;

        String mapKey = issuerId + situation;
        if (cacheValid(mapKey)) {
            activityList = cache.get(mapKey);
            if (activityList != null) {
                log.info("get from cache " + issuerId + " " + situation + ": " + activityList);
                return activityList;
            }
        }

        activityList = getActivities(issuerId);
        if (ListUtils.isEmpty(activityList)) {
            return null;
        }

        activityList = filterActivitiesBySituation(activityList, situation);

        if (ListUtils.isNotEmpty(activityList)) {
            if (!cacheValid(mapKey)) {
                synchronized (cacheValidMap) {
                    cache.put(mapKey, activityList);
                    cacheValidMap.put(mapKey, true);
                }
                log.info("set cache " + issuerId + " " + situation + ": " + activityList);
            }
        }
        return activityList;
    }

    private List<Activity> filterActivitiesBySituation(List<Activity> activityList, RewardSituation situation) {
        List<Activity> copyOfActivityList = new ArrayList(activityList.size());
        if (ListUtils.isNotEmpty(activityList)) {
            for (Activity activity : activityList) {

                List<Rule> ruleList = activity.getRules();
                if (ListUtils.isNotEmpty(ruleList)) {
                    List<Rule> situationRuleList = filterRuleListBySituation(ruleList, situation);

                    if (ListUtils.isNotEmpty(situationRuleList)) {
                        // update rule list, and set to copy of activity list.
                        activity.setRuleList(situationRuleList);
                        copyOfActivityList.add(activity);
                    }
                }
            }
        }
        return copyOfActivityList;
    }

    private List<Rule> filterRuleListBySituation(List<Rule> ruleList, RewardSituation situation) {
        List<Rule> copyOfRuleList = new ArrayList<Rule>(ruleList.size());
        if (ListUtils.isNotEmpty(ruleList)) {
            for (Rule rule : ruleList) {

                List<RuleReward> rewardList = rule.getRewardList();
                if (ListUtils.isNotEmpty(rewardList)) {
                    List<RuleReward> situationRewardList = filterRewardListBySituation(rewardList, situation);

                    if (ListUtils.isNotEmpty(situationRewardList)) {
                        // update rule reward list, and set to copy of rule list.
                        rule.setRewardList(situationRewardList);
                        copyOfRuleList.add(rule);
                    }
                }
            }
        }
        return copyOfRuleList;
    }

    private List<RuleReward> filterRewardListBySituation(List<RuleReward> rewardList, RewardSituation situation) {
        List<RuleReward> copyOfRuleRewardList = new ArrayList<RuleReward>();
        if (ListUtils.isNotEmpty(rewardList)) {
            for (RuleReward reward : rewardList) {

                if (situation.equals(reward.getSituation())) {
                    copyOfRuleRewardList.add(reward);
                }
            }
        }
        return copyOfRuleRewardList;
    }

    @Override
    public void syncActivities() {
        cacheValidMap.clear();
        log.info("clear all caches.");
    }

    @Override
    public void syncActivities(String issuerId) {
        cacheValidMap.put(issuerId, false);
        log.info("invalid cache for " + issuerId);
    }

    @Override
    public Activity saveActivityOnly(Activity activity) {
        activity.setId(activityMapper.nextActivityId());

        MActivityDTO dto = activity.toDTO();
        dto.setCreateTimestamp(new Date());
        dto.setCreateUser("ROOT"); // todo
        if (StringUtils.isEmpty(dto.getStatus())) {
            dto.setStatus(Constants.OFF);
        }
        mapper.insert(dto);

        Activity newActivity = Activity.fromDTO(dto);
        log.info("save activity:" + newActivity);
        return newActivity;
    }

    @Override
    public Activity saveActivity(Activity activity, List<Rule> ruleList) {
        if (ListUtils.isEmpty(ruleList)) {
            throw new IllegalStateException("saved ruleList should not be empty!");
        }

        Activity newActivity = saveActivityOnly(activity);

        for (Rule rule : ruleList) {
            doSaveRule(newActivity, rule);
        }
        return newActivity;
    }

    @Override
    public Activity attachRule(String activityId, Rule rule) {
        List<Rule> ruleList = new ArrayList<Rule>(1);
        ruleList.add(rule);
        return attachRules(activityId, ruleList);
    }

    @Override
    public Activity attachRules(String activityId, List<Rule> ruleList) {
        if (ListUtils.isEmpty(ruleList)) {
            throw new IllegalStateException("attached ruleList should not be empty!");
        }

        Activity activity = getActivity(activityId);
        if (activity == null) {
            throw new IllegalStateException("can`t find activity for activityId:" + activityId);
        }

        for (Rule rule : ruleList) {
            doSaveRule(activity, rule);
        }
        return activity;
    }

    private void doSaveRule(Activity activity, Rule rule) {
        // save rule
        Rule newRule = ruleDAO.saveRule(rule);

        activity.addRule(newRule);

        // save rule participation relationship
        ruleParticipationDAO.saveRuleParticipation(activity.getId(), newRule.getId());
    }

    @Override
    public void removeActivity(String activityId) {
        if (activityId == null) {
            throw new IllegalStateException("activityId should not be null!");
        }
        mapper.deleteByPrimaryKey(activityId);
        log.info("remove activity:" + activityId);

        List<MRuleParticipationDTO> participationList = ruleParticipationDAO.getRuleParticipation(activityId);
        if (ListUtils.isNotEmpty(participationList)) {
            for (MRuleParticipationDTO participation : participationList) {
                String ruleId = participation.getRuleId();
                if (StringUtils.isNotEmpty(ruleId)) {
                    // 仅删除规则，无需在此解除与活动的关联关系，clearRuleParticipation会统一清除
                    ruleDAO.removeRule(ruleId);
                    log.info("remove rule:" + ruleId + " for activity:" + activityId);
                }
            }
        }

        int removedCount = ruleParticipationDAO.clearRuleParticipation(activityId);
        log.info("remove " + removedCount + " rule participation for activity:" + activityId);
    }

    private Activity attachRules(Activity activity) {
        List<MRuleParticipationDTO> participationList = ruleParticipationDAO.getRuleParticipation(activity.getId());
        if (ListUtils.isNotEmpty(participationList)) {
            for (MRuleParticipationDTO participation : participationList) {
                String ruleId = participation.getRuleId();
                if (StringUtils.isEmpty(ruleId)) {
                    throw new NullPointerException("ruleId should not be null! for activity:" + activity);
                }

                Rule rule = ruleDAO.getRule(ruleId);
                activity.addRule(rule);
            }
        }
        return activity;
    }

    public List<Activity> getActivities(String issuerId, Date start, Date end, String mutex, String channel, String status) {
        MActivityDTOCriteria activityDTOCriteria = new MActivityDTOCriteria();
        MActivityDTOCriteria.Criteria criteria = activityDTOCriteria.createCriteria();

        criteria.andIssuerIdEqualTo(issuerId);

        if (start != null) {
            criteria.andValidStartLessThanOrEqualTo(start);
        }

        if (end != null) {
            criteria.andValidEndGreaterThan(end);
        }

        if (StringUtils.isNotEmpty(mutex)) {
            criteria.andMutexEqualTo(mutex);
        }

        if (StringUtils.isNotEmpty(channel)) {
            criteria.andChannelEqualTo(channel);
        }

        if (StringUtils.isNotEmpty(status)) {
            criteria.andStatusEqualTo(status);
        }
        activityDTOCriteria.setOrderByClause("priority desc");

        List<Activity> activityList = new ArrayList<Activity>();
        List<MActivityDTO> dtoList = mapper.selectByExample(activityDTOCriteria);
        if (ListUtils.isNotEmpty(dtoList)) {
            for (MActivityDTO dto : dtoList) {
                Activity activity = Activity.fromDTO(dto);
                if (activity != null) {
                    attachRules(activity);
                    activityList.add(activity);
                }
            }
        }
        return activityList;
    }

}
