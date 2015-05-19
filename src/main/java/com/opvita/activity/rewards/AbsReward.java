package com.opvita.activity.rewards;

import com.opvita.activity.daowrapper.RewardLogDAO;
import com.opvita.activity.daowrapper.RuleRewardDAO;
import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.model.EsOrderInfoBean;
import com.opvita.activity.model.RewardLog;
import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by rd on 2015/5/15.
 */
public abstract class AbsReward implements Rewardable {
    protected Log log = LogFactory.getLog(getClass());

    @Autowired private RewardLogDAO rewardLogDAO;
    @Autowired private RuleRewardDAO ruleRewardDAO;

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

        // 查询该用户已奖励多少次
        int rewardCount = rewardLogDAO.getRewardCount(bean, reward);

        // 奖励次数未超限才能进行奖励
        if (reward.exceedLimit(rewardCount)) {
            return false;
        }

        // 记录奖励日志，初始状态为待奖励
        RewardLog rewardLog = rewardLogDAO.saveRewardLog(bean, reward);
        log.info("log reward start. " + rewardLog);

        // 更新当前奖励数据
        ruleRewardDAO.increaseCurrentReward(reward);

        // actual do reward.
        boolean succeed = doReward(bean, rule, reward);
        if (succeed) {
            // 奖励执行成功后更新奖励日志
            rewardLogDAO.completeRewardLog(rewardLog);
            log.info("log reward complete. " + rewardLog.getId());
        }
        return succeed;
    }
}
