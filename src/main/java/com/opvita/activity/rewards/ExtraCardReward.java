package com.opvita.activity.rewards;

import com.opvita.activity.model.RuleReward;
import com.opvita.activity.dto.EsOrderItemsDTOWithBLOBs;
import com.opvita.activity.model.EsOrderInfoBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by rd on 2015/5/31.
 */
@Component("extraCardReward")
public class ExtraCardReward extends AbsNewCardReward {
    @Override
    String getRewardProductId(EsOrderInfoBean bean, RuleReward reward) {
        // 送购买卡类型一致的卡片，则根据购卡订单确定相同的商品号
        // todo 目前只支持额外赠送一张卡
        List<EsOrderItemsDTOWithBLOBs> esOrderItemList = bean.getEsOrderItemsList();
        Integer productId = esOrderItemList.get(0).getProductId();
        return String.valueOf(productId);
    }
}
