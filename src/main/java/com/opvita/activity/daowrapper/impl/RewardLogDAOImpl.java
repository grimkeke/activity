package com.opvita.activity.daowrapper.impl;

import com.opvita.activity.dao.ActivityMapper;
import com.opvita.activity.dao.MRewardLogDTOMapper;
import com.opvita.activity.daowrapper.RewardLogDAO;
import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.MRewardLogDTO;
import com.opvita.activity.dto.MRewardLogDTOCriteria;
import com.opvita.activity.enums.RewardLogStatus;
import com.opvita.activity.model.EsOrderInfoBean;
import com.opvita.activity.model.RewardLog;
import com.opvita.activity.model.RuleReward;
import com.opvita.activity.utils.ListUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rd on 2015/5/14.
 */
@Service
public class RewardLogDAOImpl implements RewardLogDAO {
    private static Log log = LogFactory.getLog(RewardLogDAOImpl.class);

    @Autowired
    private MRewardLogDTOMapper mapper;
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public RewardLog saveRewardLog(EsOrderInfoBean bean, RuleReward reward) {
        EsOrderDTO order = bean.getEsOrderDTO();

        RewardLog rewardLog = new RewardLog();
        rewardLog.setId(activityMapper.nextRewardLogId());
        rewardLog.setUserId(order.getCustSeq());
        rewardLog.setUserMobile(order.getBuyerMobile());
        rewardLog.setOrderId(order.getOrderId());
        rewardLog.setOrderSn(order.getSn());
        rewardLog.setRewardId(reward.getId());
        rewardLog.setRuleId(reward.getRuleId());
        rewardLog.setStatus(RewardLogStatus.WAIT_FOR_REWARD.toString());
        rewardLog.setCreateUser(order.getCustSeq());
        rewardLog.setCreateTimestamp(new Date());
        mapper.insert(rewardLog.toDTO());
        return rewardLog;
    }

    @Override
    public synchronized int getRewardCount(EsOrderInfoBean bean, RuleReward reward) {
        int rewardCount = getRewardLogs(bean, reward).size();
        if (rewardCount > 0) {
            log.info("userId:" + bean.getEsOrderDTO().getCustSeq() + " has reward " + rewardCount + " times. " + reward);
        }
        return rewardCount;
    }

    @Override
    public List<RewardLog> getRewardLogs(EsOrderInfoBean bean, RuleReward reward) {
        List<RewardLog> rewardLogList = new ArrayList<RewardLog>();

        MRewardLogDTOCriteria criteria = new MRewardLogDTOCriteria();
        criteria.createCriteria()
                .andRuleIdEqualTo(reward.getRuleId())
                .andRewardIdEqualTo(reward.getId())
                .andUserIdEqualTo(bean.getEsOrderDTO().getCustSeq())
                .andStatusNotEqualTo(RewardLogStatus.INVALID_LOG.toString());

        List<MRewardLogDTO> dtoList = mapper.selectByExample(criteria);
        if (ListUtils.isNotEmpty(dtoList)) {
            for (MRewardLogDTO dto : dtoList) {
                RewardLog rewardLog = RewardLog.fromDTO(dto);
                rewardLogList.add(rewardLog);
            }
        }
        return rewardLogList;
    }

    @Override
    public RewardLog completeRewardLog(RewardLog rewardLog) {
        rewardLog.setStatus(RewardLogStatus.REWARD_COMPLETE.toString());
        rewardLog.setUpdateUser(rewardLog.getCreateUser());
        rewardLog.setUpdateTimestamp(new Date());
        mapper.updateByPrimaryKey(rewardLog.toDTO());
        return rewardLog;
    }
}
