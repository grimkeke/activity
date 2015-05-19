package com.opvita.activity.rewards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by rd on 2015/4/27.
 */
@Component
public class RewardFactory {
    private static NewCardReward newCardReward;
    private static DiscountReward discountReward;
    private static PercentageReward percentageReward;

    public static Rewardable newInstance(String ruleRewardType) {
        if (Rewardable.NEW_CARD.equals(ruleRewardType)) {
            return RewardFactory.newCardReward;

        } else if (Rewardable.DISCOUNT.equals(ruleRewardType)) {
            return RewardFactory.discountReward;

        } else if (Rewardable.PERCENTAGE.equals(ruleRewardType)) {
            return RewardFactory.percentageReward;

        } else {
            throw new IllegalStateException("invalid reward type:" + ruleRewardType);
        }
    }

    @Autowired @Qualifier("newCardReward")
    public void setNewCardReward(NewCardReward newCardReward) {
        RewardFactory.newCardReward = newCardReward;
    }

    @Autowired @Qualifier("discountReward")
    public void setDiscountReward(DiscountReward discountReward) {
        RewardFactory.discountReward = discountReward;
    }

    @Autowired @Qualifier("percentageReward")
    public void setPercentageReward(PercentageReward percentageReward) {
        RewardFactory.percentageReward = percentageReward;
    }
}
