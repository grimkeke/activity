package com.opvita.activity.rewards;

import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.model.EsOrderInfoBean;
import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by rd on 2015/5/15.
 */
public abstract class AbsReward implements Rewardable {
    protected Log log = LogFactory.getLog(getClass());

    abstract boolean doReward(EsOrderInfoBean bean, Rule rule, RuleReward reward);

    @Override
    public boolean executeReward(EsOrderInfoBean bean, Rule rule, RuleReward reward) {
        EsOrderDTO esOrder = bean.getEsOrderDTO();

        // double check
        if (!rule.isSatisfied()) {
            log.warn("order:" + esOrder.getSn() + " in executeReward but not satisfied! " + rule);
            return false;
        }

        if (!reward.getRuleId().equals(rule.getId())) {
            log.warn("expect ruleId:" + reward.getRuleId() + " but get " + rule.getId());
            return false;
        }

        // actual do reward.
        return doReward(bean, rule, reward);
    }
}
