package com.opvita.activity.daowrapper.impl;

import com.opvita.activity.dao.ActivityMapper;
import com.opvita.activity.dao.MActivityRuleDTOMapper;
import com.opvita.activity.daowrapper.ActivityRuleDAO;
import com.opvita.activity.daowrapper.ProductParticipationDAO;
import com.opvita.activity.daowrapper.RuleParticipationDAO;
import com.opvita.activity.daowrapper.RuleRewardDAO;
import com.opvita.activity.dto.MActivityRuleDTO;
import com.opvita.activity.dto.MActivityRuleDTOCriteria;
import com.opvita.activity.dto.MProductParticipationDTO;
import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import com.opvita.activity.utils.ListUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by rd on 2015/4/25.
 */
@Service
public class ActivityRuleDAOImpl implements ActivityRuleDAO {
    private static Log log = LogFactory.getLog(ActivityRuleDAOImpl.class);
    @Autowired
    private MActivityRuleDTOMapper mapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private RuleRewardDAO ruleRewardDAO;
    @Autowired
    private ProductParticipationDAO productParticipationDAO;
    @Autowired
    private RuleParticipationDAO ruleParticipationDAO;

    @Override
    public List<Rule> getRules(List<String> ruleIds) {
        List<Rule> ruleList = null;

        MActivityRuleDTOCriteria criteria = new MActivityRuleDTOCriteria();
        criteria.createCriteria().andIdIn(ruleIds);

        List<MActivityRuleDTO> dtoList = mapper.selectByExample(criteria);
        if (ListUtils.isNotEmpty(dtoList)) {
            ruleList = new ArrayList<Rule>(dtoList.size());

            for (MActivityRuleDTO dto : dtoList) {
                Rule rule = Rule.fromDTO(dto);
                ruleList.add(attachRuleInfo(rule));
            }
            // 按规则优先级倒序排序
            Collections.sort(ruleList, new Rule());
        }
        return ruleList;
    }

    private Rule attachRuleInfo(Rule rule) {
        List<RuleReward> dataList = ruleRewardDAO.getReward(rule.getId());
        rule.setRewardList(dataList);

        List<MProductParticipationDTO> participationList = productParticipationDAO.getProductParticipation(rule.getId());
        if (ListUtils.isNotEmpty(participationList)) {
            for (MProductParticipationDTO participation : participationList) {
                rule.addProductParticipate(participation.getProductId());
            }
        }
        return rule;
    }

    @Override
    public Rule getRule(String ruleId) {
        Rule rule = getRuleOnly(ruleId);
        if (rule == null) {
            return null;
        }
        return attachRuleInfo(rule);
    }

    @Override
    public Rule getRuleOnly(String ruleId) {
        MActivityRuleDTO dto = mapper.selectByPrimaryKey(ruleId);
        return Rule.fromDTO(dto);
    }

    @Override
    public Rule saveRule(Rule rule) {
        if (rule == null) {
            throw new NullPointerException("rule should not be null!");
        }
        return saveRule(rule, rule.getRewardList());
    }

    @Override
    public Rule saveRule(Rule rule, List<RuleReward> dataList) {
        if (ListUtils.isEmpty(dataList)) {
            throw new IllegalStateException("rule data should not be empty! rule:" + rule);
        }

        Rule newRule = doSave(rule);

        productParticipationDAO.saveProductParticipation(newRule.getId(), newRule.getProductSet());

        // 保存规则数据
        List<RuleReward> newList = new ArrayList<RuleReward>(dataList.size());
        for (RuleReward data : dataList) {
            data.setRuleId(newRule.getId());
            data.setCreateUser(newRule.getCreateUser());
            RuleReward newData = ruleRewardDAO.saveReward(data);
            newList.add(newData);
        }
        newRule.setRewardList(newList);

        log.info("saved rule:" + newRule);
        return newRule;
    }

    @Override
    public void detachRule(String activityId, String ruleId) {
        if (activityId == null) {
            throw new NullPointerException("activityId should not be null!");
        }

        removeRule(ruleId);

        int removedCount = ruleParticipationDAO.removeRuleParticipation(activityId, ruleId);
        log.info("remove " + removedCount + " participation from activity:" + activityId + " for ruleId:" + ruleId);
    }

    @Override
    public void removeRule(String ruleId) {
        if (ruleId == null) {
            throw new NullPointerException("ruleId should not be null!");
        }

        mapper.deleteByPrimaryKey(ruleId);
        log.info("remove rule:" + ruleId);

        int removedCount = productParticipationDAO.clearProductParticipation(ruleId);
        log.info("remove " + removedCount + " product participation for ruleId:" + ruleId);

        removedCount = ruleRewardDAO.removeReward(ruleId);
        log.info("remove " + removedCount + " rule data for ruleId:" + ruleId);
    }

    private Rule doSave(Rule rule) {
        rule.setId(activityMapper.nextActivityRuleId());
        rule.setCreateUser("ROOT"); // todo
        rule.setCreateTimestamp(new Date());

        MActivityRuleDTO dto = rule.toDTO();
        mapper.insert(dto);
        return rule;
    }
}
