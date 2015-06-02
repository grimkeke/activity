package com.opvita.activity.rewards;

import com.opvita.activity.enums.RewardType;
import com.opvita.activity.model.RuleReward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by rd on 2015/4/27.
 */
@Component
public class RewardFactory {
    private static NewCardReward newCardReward;
    private static ExtraCardReward extraCardReward;
    private static DiscountReward discountReward;
    private static PercentageReward percentageReward;
    private static RechargePCardReward rechargePCardReward;
    private static CashierRechargeReward cashierRechargeReward;
    private static SaleReward saleReward;

    public static Rewardable newInstance(RewardType rewardType) {
        switch (rewardType) {
            case NEW_CARD:
                return RewardFactory.newCardReward;
            case EXTRA_CARD:
                return RewardFactory.extraCardReward;
            case DISCOUNT:
                return RewardFactory.discountReward;
            case PERCENTAGE:
                return RewardFactory.percentageReward;
            case RECHARGE_PCARD:
                return RewardFactory.rechargePCardReward;
            case CASHIER_RECHARGE:
                return RewardFactory.cashierRechargeReward;
            case SALE:
                return RewardFactory.saleReward;
            default:
                throw new IllegalStateException("invalid reward type:" + rewardType);
        }
    }

    // 创建一个减扣的奖励
    public static RuleReward newDiscountReward(int value, int maxRewards, int rewardsPerPerson) {
        RuleReward ruleReward = RuleReward.newInstance(RewardType.DISCOUNT, maxRewards, rewardsPerPerson);
        ruleReward.setRewardValue(Long.valueOf(value));
        return ruleReward;
    }

    // 创建一个打折的奖励
    public static RuleReward newPercentageReward(int value, int maxRewards, int rewardsPerPerson) {
        RuleReward ruleReward = RuleReward.newInstance(RewardType.PERCENTAGE, maxRewards, rewardsPerPerson);
        ruleReward.setRewardValue(Long.valueOf(value));
        return ruleReward;
    }

    // 创建一个开新卡的奖励
    public static RuleReward newProductReward(String productId, int maxRewards, int rewardsPerPerson) {
        RuleReward ruleReward = RuleReward.newInstance(RewardType.NEW_CARD, maxRewards, rewardsPerPerson);
        ruleReward.setRewardProduct(productId);
        return ruleReward;
    }

    // 创建一个额外送卡的奖励
    public static RuleReward newExtraProductReward(int maxRewards, int rewardsPerPerson) {
        RuleReward ruleReward = RuleReward.newInstance(RewardType.EXTRA_CARD, maxRewards, rewardsPerPerson);
        return ruleReward;
    }

    // 创建一个满额换购奖励
    public static RuleReward newSaleProductReward(int maxRewards, int rewardsPerPerson) {
        RuleReward ruleReward = RuleReward.newInstance(RewardType.SALE, maxRewards, rewardsPerPerson);
        return ruleReward;
    }

    // 创建一个充值主卡的奖励
    public static RuleReward newRechargePCardReward(int value, int maxRewards, int rewardsPerPerson) {
        RuleReward ruleReward = RuleReward.newInstance(RewardType.RECHARGE_PCARD, maxRewards, rewardsPerPerson);
        ruleReward.setRewardValue(Long.valueOf(value));
        return ruleReward;
    }

    // 创建一个充值操作员主卡的奖励
    public static RuleReward newCashierRechargeReward(int value, int maxRewards, int rewardsPerPerson) {
        RuleReward ruleReward = RuleReward.newInstance(RewardType.CASHIER_RECHARGE, maxRewards, rewardsPerPerson);
        ruleReward.setRewardValue(Long.valueOf(value));
        return ruleReward;
    }

    @Autowired @Qualifier("newCardReward")
    public void setNewCardReward(NewCardReward newCardReward) {
        RewardFactory.newCardReward = newCardReward;
    }

    @Autowired @Qualifier("extraCardReward")
    public void setExtraCardReward(ExtraCardReward extraCardReward) {
        RewardFactory.extraCardReward = extraCardReward;
    }

    @Autowired @Qualifier("discountReward")
    public void setDiscountReward(DiscountReward discountReward) {
        RewardFactory.discountReward = discountReward;
    }

    @Autowired @Qualifier("percentageReward")
    public void setPercentageReward(PercentageReward percentageReward) {
        RewardFactory.percentageReward = percentageReward;
    }

    @Autowired @Qualifier("rechargePCardReward")
    public void setRechargePCardReward(RechargePCardReward rechargePCardReward) {
        RewardFactory.rechargePCardReward = rechargePCardReward;
    }

    @Autowired @Qualifier("cashierRechargeReward")
    public void setCashierRechargeReward(CashierRechargeReward cashierRechargeReward) {
        RewardFactory.cashierRechargeReward = cashierRechargeReward;
    }

    @Autowired @Qualifier("saleReward")
    public void setSaleReward(SaleReward saleReward) {
        RewardFactory.saleReward = saleReward;
    }
}
