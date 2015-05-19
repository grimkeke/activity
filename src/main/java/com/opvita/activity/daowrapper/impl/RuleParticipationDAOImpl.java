package com.opvita.activity.daowrapper.impl;

import com.opvita.activity.utils.ListUtils;
import com.opvita.activity.common.Constants;
import com.opvita.activity.dao.ActivityMapper;
import com.opvita.activity.dao.MRuleParticipationDTOMapper;
import com.opvita.activity.daowrapper.RuleParticipationDAO;
import com.opvita.activity.dto.MRuleParticipationDTO;
import com.opvita.activity.dto.MRuleParticipationDTOCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by rd on 2015/4/27.
 */
@Service
public class RuleParticipationDAOImpl implements RuleParticipationDAO {
    @Autowired private MRuleParticipationDTOMapper mapper;
    @Autowired private ActivityMapper activityMapper;

    @Override
    public MRuleParticipationDTO saveRuleParticipation(String activityId, String ruleId) {
        MRuleParticipationDTO participation = new MRuleParticipationDTO();
        participation.setId(activityMapper.nextRuleParticipationId());
        participation.setActivityId(activityId);
        participation.setRuleId(ruleId);
        participation.setStatus(Constants.ON);
        participation.setCreateUser("ROOT"); // todo
        participation.setCreateTimestamp(new Date());
        mapper.insert(participation);
        return participation;
    }

    @Override
    public List<MRuleParticipationDTO> getRuleParticipation(String activityId) {
        MRuleParticipationDTOCriteria criteria = new MRuleParticipationDTOCriteria();
        criteria.createCriteria().andActivityIdEqualTo(activityId);
        return mapper.selectByExample(criteria);
    }

    @Override
    public List<MRuleParticipationDTO> getActivityParticipation(String ruleId) {
        MRuleParticipationDTOCriteria criteria = new MRuleParticipationDTOCriteria();
        criteria.createCriteria().andRuleIdEqualTo(ruleId);
        return mapper.selectByExample(criteria);
    }

    @Override
    public boolean isValidActivityAndRule(String activityId, String ruleId) {
        MRuleParticipationDTOCriteria criteria = new MRuleParticipationDTOCriteria();
        criteria.createCriteria().andActivityIdEqualTo(activityId).andRuleIdEqualTo(ruleId);
        return ListUtils.isNotEmpty(mapper.selectByExample(criteria));
    }

    @Override
    public int clearRuleParticipation(String activityId) {
        MRuleParticipationDTOCriteria criteria = new MRuleParticipationDTOCriteria();
        criteria.createCriteria().andActivityIdEqualTo(activityId);
        return mapper.deleteByExample(criteria);
    }

    @Override
    public int invalidateRuleParticipation(String activityId) {
        MRuleParticipationDTOCriteria criteria = new MRuleParticipationDTOCriteria();
        criteria.createCriteria().andActivityIdEqualTo(activityId);

        MRuleParticipationDTO dto = new MRuleParticipationDTO();
        dto.setStatus(Constants.OFF);
        return mapper.updateByExampleSelective(dto, criteria);
    }

    @Override
    public int removeRuleParticipation(String activityId, String ruleId) {
        MRuleParticipationDTOCriteria criteria = new MRuleParticipationDTOCriteria();
        criteria.createCriteria().andActivityIdEqualTo(activityId).andRuleIdEqualTo(ruleId);
        return mapper.deleteByExample(criteria);
    }

    @Override
    public int invalidateRuleParticipation(String activityId, String ruleId) {
        MRuleParticipationDTOCriteria criteria = new MRuleParticipationDTOCriteria();
        criteria.createCriteria().andActivityIdEqualTo(activityId).andRuleIdEqualTo(ruleId);

        MRuleParticipationDTO dto = new MRuleParticipationDTO();
        dto.setStatus(Constants.OFF);
        return mapper.updateByExampleSelective(dto, criteria);
    }
}
