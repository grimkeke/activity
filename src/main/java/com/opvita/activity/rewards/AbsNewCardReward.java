package com.opvita.activity.rewards;

import com.opvita.activity.daowrapper.ProductInfoDAO;
import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTOWithBLOBs;
import com.opvita.activity.model.EsOrderInfoBean;
import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rd on 2015/4/27.
 */
public abstract class AbsNewCardReward extends AbsReward {

    @Autowired private ProductInfoDAO productInfoDAO;

    // 获取奖励商品的商品号
    abstract String getRewardProductId(EsOrderInfoBean bean, RuleReward reward);

    @Override
    boolean doReward(EsOrderInfoBean bean, Rule rule, RuleReward reward) {
        EsOrderDTO esOrder = bean.getEsOrderDTO();
        List<EsOrderItemsDTOWithBLOBs> esOrderItemList = bean.getEsOrderItemsList();

        String rewardProductId = getRewardProductId(bean, reward);
        log.info(String.format("order:%s satisfy reward:%s due to %s %s, system will bind card to user:%s",
                esOrder.getSn(), reward.getId(), reward.getRewardType(), rewardProductId,
                esOrder.getBuyerMobile() + "@" + esOrder.getCustSeq()));

        Long rewardValue = productInfoDAO.getProductCardPrice(rewardProductId); // 单位为分
        BigDecimal productPrice = new BigDecimal(rewardValue).divide(new BigDecimal(100));  // 单位为元

        return true;
    }
}
