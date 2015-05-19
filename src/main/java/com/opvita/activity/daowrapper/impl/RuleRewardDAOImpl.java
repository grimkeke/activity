package com.opvita.activity.daowrapper.impl;

import com.opvita.activity.dao.ActivityMapper;
import com.opvita.activity.dao.MRuleRewardDTOMapper;
import com.opvita.activity.daowrapper.ActivityRuleDAO;
import com.opvita.activity.daowrapper.RuleRewardDAO;
import com.opvita.activity.dto.MRuleRewardDTO;
import com.opvita.activity.dto.MRuleRewardDTOCriteria;
import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import com.opvita.activity.utils.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rd on 2015/4/25.
 */
@Service
public class RuleRewardDAOImpl implements RuleRewardDAO {
    @Autowired
    private MRuleRewardDTOMapper mapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ActivityRuleDAO ruleDAO;

    @Override
    public RuleReward saveReward(RuleReward data) {
        data.setId(activityMapper.nextRuleRewardId());

        MRuleRewardDTO dto = data.toDTO();
        dto.setCreateTimestamp(new Date());
        mapper.insert(dto);
        return RuleReward.fromDTO(dto);
    }

    @Override
    public List<RuleReward> getReward(String ruleId) {
        List<RuleReward> dataList = null;
        MRuleRewardDTOCriteria criteria = new MRuleRewardDTOCriteria();
        criteria.createCriteria().andRuleIdEqualTo(ruleId);

        List<MRuleRewardDTO> dtoList = mapper.selectByExample(criteria);
        if (ListUtils.isNotEmpty(dtoList)) {
            dataList = new ArrayList<RuleReward>(dtoList.size());

            Rule rule = ruleDAO.getRuleOnly(ruleId);
            if (rule == null) {
                throw new IllegalStateException("ruleId:" + ruleId + " for rule should not be empty!");
            }

            for (MRuleRewardDTO dto : dtoList) {
                RuleReward data = RuleReward.fromDTO(dto);
                dataList.add(data);
            }
        }
        return dataList;
    }

    @Override
    public int removeReward(String ruleId) {
        MRuleRewardDTOCriteria criteria = new MRuleRewardDTOCriteria();
        criteria.createCriteria().andRuleIdEqualTo(ruleId);
        return mapper.deleteByExample(criteria);
    }

    @Override
    public synchronized RuleReward increaseCurrentReward(RuleReward reward) {
        long next = reward.getCurrentRewards() + 1;
        reward.setCurrentRewards(next);
        reward.setUpdateUser(StringUtils.isNotEmpty(reward.getUpdateUser()) ? reward.getUpdateUser() : reward.getCreateUser());
        reward.setUpdateTimestamp(new Date());
        mapper.updateByPrimaryKey(reward.toDTO());
        return reward;
    }
}
