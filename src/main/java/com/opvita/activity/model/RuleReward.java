package com.opvita.activity.model;

import com.opvita.activity.enums.RewardSituation;
import com.opvita.activity.enums.RewardType;
import com.opvita.activity.rewards.RewardFactory;
import com.opvita.activity.rewards.Rewardable;
import com.opvita.activity.common.Constants;
import com.opvita.activity.dto.MRuleRewardDTO;
import com.opvita.activity.model.EsOrderInfoBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by rd on 2015/4/25.
 */
public class RuleReward extends MRuleRewardDTO {
    private static Log log = LogFactory.getLog(RuleReward.class);

    // 奖励行为
    private Rewardable rewardable;

    // 何时执行奖励
    private RewardSituation situation;

    private RuleReward() {
    }

    public void setRewardable(Rewardable rewardable) {
        this.rewardable = rewardable;
    }

    public void setSituation(RewardSituation situation) {
        this.situation = situation;
    }

    public RewardSituation getSituation() {
        return situation;
    }

    public synchronized boolean exceedLimit(int rewardCount) {
        if (rewardCount < 0) {
            throw new IllegalStateException("invalid have rewards:" + rewardCount);
        }

        long max = getMaxRewards();
        long current = getCurrentRewards();
        short rewardsPerPerson = getRewardsPerPerson();

        if (current >= max) {
            log.warn("ruleId:" + getRuleId() + " exceed max rewards:" + max + " current:" + current);
            return true;
        }

        if (rewardCount >= rewardsPerPerson) {
            log.warn("ruleId:" + getRuleId() + " exceed rewards/person:" + rewardsPerPerson + " have rewards:" + rewardCount);
            return true;
        }
        return false;
    }

    public boolean executeReward(Rule rule, EsOrderInfoBean bean) {
        boolean succeed = false;
        // 只有达到奖励区间条件时，并且没有超过奖励限制，才享受奖励规则
        if (rule != null &&
                rule.isValid() &&
                rule.isSatisfied()) {
            succeed = rewardable.executeReward(bean, rule, this);
        }
        return succeed;
    }

    // 使用RewardFactory静态方法创建，如newCashierRechargeReward(int, int, int)
    public static RuleReward newInstance(RewardType rewardType, int maxRewards, int rewardsPerPerson) {
        RuleReward ruleReward = new RuleReward();
        ruleReward.setMaxRewards(Long.valueOf(maxRewards));
        ruleReward.setCurrentRewards(Long.valueOf(0));
        ruleReward.setRewardsPerPerson((short) rewardsPerPerson);
        ruleReward.setStatus(Constants.ON);
        ruleReward.setRewardType(rewardType);
        ruleReward.setRewardable(RewardFactory.newInstance(rewardType));
        ruleReward.setSituation(getRewardSituation(rewardType));
        return ruleReward;
    }

    // 通过奖励类型决定奖励发生在何时
    private static RewardSituation getRewardSituation(RewardType rewardType) {
        switch (rewardType) {
            case DISCOUNT:
            case PERCENTAGE:
            case SALE:
                return RewardSituation.BEFORE_MAKE_ORDER;

            case NEW_CARD:
            case EXTRA_CARD:
            case RECHARGE_PCARD:
            case CASHIER_RECHARGE:
                return RewardSituation.AFTER_PAY_SUCCESS;

            default:
                throw new IllegalStateException("invalid rewardType:" + rewardType);
        }
    }

    public void setRewardType(RewardType rewardType) {
        super.setRewardType(rewardType.toString());
    }

    public MRuleRewardDTO toDTO() {
        MRuleRewardDTO dto = new MRuleRewardDTO();
        dto.setId(getId());
        dto.setRuleId(getRuleId());
        dto.setRewardValue(getRewardValue());
        dto.setRewardProduct(getRewardProduct());
        dto.setRewardType(getRewardType());
        dto.setMaxRewards(getMaxRewards());
        dto.setCurrentRewards(getCurrentRewards());
        dto.setRewardsPerPerson(getRewardsPerPerson());
        dto.setStatus(getStatus());
        dto.setCreateUser(getCreateUser());
        dto.setCreateTimestamp(getCreateTimestamp());
        dto.setUpdateUser(getUpdateUser());
        dto.setUpdateTimestamp(getUpdateTimestamp());
        return dto;
    }

    public static RuleReward fromDTO(MRuleRewardDTO dto) {
        if (dto == null) {
            return null;
        }

        RuleReward data = new RuleReward();
        data.setId(dto.getId());
        data.setRuleId(dto.getRuleId());
        data.setRewardValue(dto.getRewardValue());
        data.setRewardProduct(dto.getRewardProduct());
        data.setRewardType(dto.getRewardType());
        data.setMaxRewards(dto.getMaxRewards());
        data.setCurrentRewards(dto.getCurrentRewards());
        data.setRewardsPerPerson(dto.getRewardsPerPerson());
        data.setStatus(dto.getStatus());
        data.setCreateUser(dto.getCreateUser());
        data.setCreateTimestamp(dto.getCreateTimestamp());
        data.setUpdateUser(dto.getUpdateUser());
        data.setUpdateTimestamp(dto.getUpdateTimestamp());

        RewardType theType = RewardType.valueOf(data.getRewardType());
        data.setRewardable(RewardFactory.newInstance(theType));
        data.setSituation(getRewardSituation(theType));
        return data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(rewardable.getClass().getSimpleName() + "{" + getRewardType() + "@" + getId());
        sb.append(", ruleId='").append(getRuleId()).append('\'');
        if (getRewardValue() != null) {
            sb.append(", value=").append(getRewardValue());
        }
        if (getRewardProduct() != null) {
            sb.append(", productId='").append(getRewardProduct()).append('\'');
        }
        sb.append(", cur/max@per=" + getCurrentRewards() + "/" + getMaxRewards() + "@" + getRewardsPerPerson());
        sb.append('}');
        return sb.toString();
    }
}
