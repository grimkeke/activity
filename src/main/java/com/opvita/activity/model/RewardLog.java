package com.opvita.activity.model;

import com.opvita.activity.dto.MRewardLogDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by rd on 2015/5/14.
 */
public class RewardLog extends MRewardLogDTO {
    private static Log log = LogFactory.getLog(RewardLog.class);

    public MRewardLogDTO toDTO() {
        MRewardLogDTO dto = new MRewardLogDTO();
        dto.setId(getId());
        dto.setUserId(getUserId());
        dto.setUserMobile(getUserMobile());
        dto.setOrderId(getOrderId());
        dto.setOrderSn(getOrderSn());
        dto.setRewardId(getRewardId());
        dto.setRuleId(getRuleId());
        dto.setStatus(getStatus());
        dto.setCreateUser(getCreateUser());
        dto.setCreateTimestamp(getCreateTimestamp());
        dto.setUpdateUser(getUpdateUser());
        dto.setUpdateTimestamp(getUpdateTimestamp());
        return dto;
    }

    public static RewardLog fromDTO(MRewardLogDTO dto) {
        if (dto == null) {
            return null;
        }

        RewardLog rewardLog = new RewardLog();
        rewardLog.setId(dto.getId());
        rewardLog.setUserId(dto.getUserId());
        rewardLog.setUserMobile(dto.getUserMobile());
        rewardLog.setOrderId(dto.getOrderId());
        rewardLog.setOrderSn(dto.getOrderSn());
        rewardLog.setRewardId(dto.getRewardId());
        rewardLog.setRuleId(dto.getRuleId());
        rewardLog.setStatus(dto.getStatus());
        rewardLog.setCreateUser(dto.getCreateUser());
        rewardLog.setCreateTimestamp(dto.getCreateTimestamp());
        rewardLog.setUpdateUser(dto.getUpdateUser());
        rewardLog.setUpdateTimestamp(dto.getUpdateTimestamp());
        return rewardLog;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RuleLog{@" + getId());
        sb.append(", userId='").append(getUserId()).append('\'');
        sb.append(", userMobile='").append(getUserMobile()).append('\'');
        sb.append(", orderId=").append(getOrderId());
        sb.append(", orderSn='").append(getOrderSn()).append('\'');
        sb.append(", rewardId='").append(getRewardId()).append('\'');
        sb.append(", ruleId='").append(getRuleId()).append('\'');
        sb.append(", status='").append(getStatus()).append('\'');
        sb.append(", createUser='").append(getCreateUser()).append('\'');
        sb.append(", createTimestamp=").append(getCreateTimestamp());
        sb.append(", updateUser='").append(getUpdateUser()).append('\'');
        sb.append(", updateTimestamp=").append(getUpdateTimestamp());
        sb.append('}');
        return sb.toString();
    }
}
