package com.opvita.activity.rewards;

import com.opvita.activity.daowrapper.ProductInfoDAO;
import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTOWithBLOBs;
import com.opvita.activity.model.EsOrderInfoBean;
import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rd on 2015/4/27.
 * 奖励新卡
 */
@Service("newCardReward")
public class NewCardReward extends AbsReward {
    private static Log log = LogFactory.getLog(NewCardReward.class);

    @Autowired private ProductInfoDAO productInfoDAO;

    @Override
    boolean doReward(EsOrderInfoBean bean, Rule rule, RuleReward reward) {
        EsOrderDTO esOrder = bean.getEsOrderDTO();
        List<EsOrderItemsDTOWithBLOBs> esOrderItemList = bean.getEsOrderItemsList();

        log.info(String.format("order:%s satisfy reward:%s due to %s %s, system will bind card to user:%s",
                esOrder.getSn(), reward.getId(), reward.getRewardType(), reward.getRewardProduct(),
                esOrder.getBuyerMobile() + "@" + esOrder.getCustSeq()));

        String rewardProductId = reward.getRewardProduct();
        Long rewardValue = productInfoDAO.getProductCardPrice(rewardProductId); // 单位为分
        BigDecimal productPrice = new BigDecimal(rewardValue).divide(new BigDecimal(100));  // 单位为元

        // 省略库存和卖卡部分
        return true;
    }
}
