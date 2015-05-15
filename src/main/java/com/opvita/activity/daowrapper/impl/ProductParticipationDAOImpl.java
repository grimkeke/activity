package com.opvita.activity.daowrapper.impl;

import com.opvita.activity.common.Constants;
import com.opvita.activity.dao.ActivityMapper;
import com.opvita.activity.dao.MProductParticipationDTOMapper;
import com.opvita.activity.daowrapper.ProductParticipationDAO;
import com.opvita.activity.dto.MProductParticipationDTO;
import com.opvita.activity.dto.MProductParticipationDTOCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by rd on 2015/4/27.
 */
@Service
public class ProductParticipationDAOImpl implements ProductParticipationDAO {
    @Autowired private MProductParticipationDTOMapper mapper;
    @Autowired private ActivityMapper activityMapper;

    @Override
    public List<MProductParticipationDTO> saveProductParticipation(String ruleId, Set<String> productSet) {
        List<MProductParticipationDTO> participationList = new ArrayList<MProductParticipationDTO>();
        if (productSet == null || productSet.isEmpty()) {
            // 空集合表示所有商品均参与活动
            MProductParticipationDTO participation = doSave(ruleId, null);
            participationList.add(participation);

        } else {
            for (String productId : productSet) {
                MProductParticipationDTO participation = doSave(ruleId, productId);
                participationList.add(participation);
            }
        }
        return participationList;
    }

    @Override
    public List<MProductParticipationDTO> getProductParticipation(String ruleId) {
        MProductParticipationDTOCriteria criteria = new MProductParticipationDTOCriteria();
        criteria.createCriteria().andRuleIdEqualTo(ruleId);
        return mapper.selectByExample(criteria);
    }

    @Override
    public int clearProductParticipation(String ruleId) {
        MProductParticipationDTOCriteria criteria = new MProductParticipationDTOCriteria();
        criteria.createCriteria().andRuleIdEqualTo(ruleId);
        return mapper.deleteByExample(criteria);
    }

    private MProductParticipationDTO doSave(String ruleId, String productId) {
        MProductParticipationDTO participation = new MProductParticipationDTO();
        participation.setId(activityMapper.nextProductParticipationId());
        participation.setRuleId(ruleId);
        participation.setProductId(productId);
        participation.setStatus(Constants.ON);
        participation.setCreateUser("ROOT"); // todo
        participation.setCreateTimestamp(new Date());
        mapper.insert(participation);
        return participation;
    }
}
