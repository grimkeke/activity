package com.opvita.activity;

import com.opvita.activity.common.Constants;
import com.opvita.activity.daowrapper.ActivityDAO;
import com.opvita.activity.daowrapper.ActivityRuleDAO;
import com.opvita.activity.daowrapper.RuleParticipationDAO;
import com.opvita.activity.enums.RewardSituation;
import com.opvita.activity.model.Activity;
import com.opvita.activity.model.EsOrderInfoBean;
import com.opvita.activity.model.Rule;
import com.opvita.activity.model.RuleReward;
import com.opvita.activity.qualify.Qualify;
import com.opvita.activity.service.ActivityService;
import com.opvita.activity.service.WapPayService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rd on 2015/4/26.
 */
@TransactionConfiguration(defaultRollback = false)
public class ActivityServiceTests extends RollbackableTest {
    private static Log log = LogFactory.getLog(ActivityServiceTests.class);

    @Autowired private ActivityService service;
    @Autowired private ActivityDAO activityDAO;
    @Autowired private ActivityRuleDAO ruleDAO;
    @Autowired private RuleParticipationDAO ruleParticipationDAO;
    @Autowired private WapPayService wapPayService;

    private String issuerId = "HFBD";

    @Test
    public void test_execute_activity() {
//        String outTradeNo = "1431669335008173918"; // 800
        String outTradeNo = "1431677343443808520"; // 500
        EsOrderInfoBean bean = wapPayService.getEsOrderInfoBySn(outTradeNo);
        service.executeActivity(bean, RewardSituation.BEFORE_MAKE_ORDER);
        service.executeActivity(bean, RewardSituation.AFTER_PAY_SUCCESS);

//        log.info("##### test get from cache #######");
//        bean = wapPayService.getEsOrderInfoBySn(outTradeNo);
//        service.executeActivity(bean, RewardSituation.BEFORE_MAKE_ORDER);
//        service.executeActivity(bean, RewardSituation.AFTER_PAY_SUCCESS);
    }

    @Test
    public void test_execute_rules() {
        List<String> ruleIds = new ArrayList<String>() {{
            add("0000000174");
            add("0000000175");
            add("0000000176");
            add("0000000177");
        }};

        String outTradeNo = "1431677343443808520"; // 500
        EsOrderInfoBean bean = wapPayService.getEsOrderInfoBySn(outTradeNo);

        service.executeRules(bean, ruleIds);
    }

    @Test
    public void test_check_order() {
        String outTradeNo = "1431677343443808520";
        EsOrderInfoBean bean = wapPayService.getEsOrderInfoBySn(outTradeNo);

        List<Activity> executorList = service.getSatisfiedActivities(bean, RewardSituation.BEFORE_MAKE_ORDER);
        log.info(executorList);

        log.info("##### test get from " + RewardSituation.AFTER_PAY_SUCCESS + " #######");
        executorList = service.getSatisfiedActivities(bean, RewardSituation.AFTER_PAY_SUCCESS);
        log.info(executorList);
    }

    @Test
    public void import_data() {
        test_activity_save();
        test_activity_save_and_attach_rule();
    }

    @Test
    public void test_activity_save_mutex() {
        Activity activity = new Activity();
        activity.setIssuerId(issuerId);
        activity.setTitle("互斥活动");
        activity.setRemark("本活动不可与其他活动共享!");
        activity.setStatus(Constants.ON);
        activity.setValidStart(newDay(-5));
        activity.setValidEnd(newDay(20));
        activity.setMutex(Constants.ON);
        activity.setPriority(new BigDecimal(999));

        List<Rule> ruleList = new ArrayList<Rule>() {{
            Rule rule1 = new Rule();
            rule1.setIssuerId(issuerId);
            rule1.setStatus(Constants.ON);
            rule1.setQualification(Qualify.NOMINAL);
            rule1.setMutex(Constants.ON);
            rule1.setSectionBegin(Long.valueOf(20000));
            rule1.setSectionEnd(Long.valueOf(40000));
            rule1.setPriority(new BigDecimal(100));
            rule1.setRewardList(new ArrayList<RuleReward>() {{
                add(RuleReward.newProductReward("15413901", 10000, 1));
            }});
            add(rule1);

            Rule rule2 = new Rule();
            rule2.setIssuerId(issuerId);
            rule2.setStatus(Constants.ON);
            rule2.setQualification(Qualify.NOMINAL);
            rule2.setMutex(Constants.ON);
            rule2.setSectionBegin(Long.valueOf(40000));
            rule2.setSectionEnd(Long.valueOf(60000));
            rule2.setPriority(new BigDecimal(99));
            rule2.setRewardList(new ArrayList<RuleReward>() {{
                add(RuleReward.newProductReward("15413900", 10000, 1));
            }});
            add(rule2);

            Rule rule3 = new Rule();
            rule3.setIssuerId(issuerId);
            rule3.setStatus(Constants.ON);
            rule3.setQualification(Qualify.NOMINAL);
            rule3.setMutex(Constants.ON);
            rule3.setSectionBegin(Long.valueOf(60000));
            rule3.setSectionEnd(Long.valueOf(80000));
            rule3.setPriority(new BigDecimal(98));
            rule3.setRewardList(new ArrayList<RuleReward>() {{
                add(RuleReward.newDiscountReward(10000, 10000, 1)); // 减去10000分
            }});
            add(rule3);

            Rule rule4 = new Rule();
            rule4.setIssuerId(issuerId);
            rule4.setStatus(Constants.ON);
            rule4.setQualification(Qualify.NOMINAL);
            rule4.setMutex(Constants.ON);
            rule4.setSectionBegin(Long.valueOf(80000));
            rule4.setPriority(new BigDecimal(97));
            rule4.setRewardList(new ArrayList<RuleReward>() {{
                add(RuleReward.newPercentageReward(80, 10000, 1)); // 8折
            }});
            add(rule4);
        }};

        Activity newActivity = activityDAO.saveActivity(activity, ruleList);
        log.info(newActivity);
    }

    // 测试一次性保存活动和规则列表(saveActivityOnly(activity, ruleList))
    @Test
    public void test_activity_save() {
        Activity activity = new Activity();
        activity.setIssuerId(issuerId);
        activity.setTitle("测试活动1");
        activity.setRemark("本活动可与其他活动共享");
        activity.setStatus(Constants.ON);
        activity.setValidStart(newDay(-5));
        activity.setValidEnd(newDay(17));
        activity.setMutex(Constants.OFF);
        activity.setPriority(new BigDecimal(100));

        List<Rule> ruleList = new ArrayList<Rule>() {{
            Rule rule1 = new Rule();
            rule1.setIssuerId(issuerId);
            rule1.setStatus(Constants.ON);
            rule1.setQualification(Qualify.NOMINAL);
            rule1.setMutex(Constants.OFF);
            rule1.setSectionBegin(Long.valueOf(50000));
            rule1.setSectionEnd(Long.valueOf(100000));
            rule1.setPriority(new BigDecimal(100));
            rule1.setRewardList(new ArrayList<RuleReward>() {{
                add(RuleReward.newPercentageReward(90, 10000, 1)); // 9折, 共10000名额，每人使用一次
            }});
            add(rule1);

            Rule rule2 = new Rule();
            rule2.setIssuerId(issuerId);
            rule2.setStatus(Constants.ON);
            rule2.setQualification(Qualify.NOMINAL);
            rule2.setMutex(Constants.OFF);
            rule2.setSectionBegin(Long.valueOf(100000));
            rule2.setPriority(new BigDecimal(100));
            rule2.setRewardList(new ArrayList<RuleReward>() {{
                add(RuleReward.newPercentageReward(80, 10000, 1)); // 8折
            }});
            add(rule2);
        }};

        Activity newActivity = activityDAO.saveActivity(activity, ruleList);
        log.info(newActivity);
    }

    // 测试首先保存活动，然后向活动添加规则（attachRule, attachRules)
    @Test
    public void test_activity_save_and_attach_rule() {
        Activity activity = new Activity();
        activity.setIssuerId(issuerId);
        activity.setTitle("万圣节活动");
        activity.setRemark("万圣节活动与其他活动共享");
        activity.setStatus(Constants.ON);
        activity.setValidStart(newDay(-5));
        activity.setValidEnd(newDay(10));
        activity.setMutex(Constants.OFF);
        activity.setPriority(new BigDecimal(99));
        Activity newActivity = activityDAO.saveActivityOnly(activity);

        Rule rule = new Rule();
        rule.setIssuerId(issuerId);
        rule.setStatus(Constants.ON);
        rule.setQualification(Qualify.PAYMENT);
        rule.setMutex(Constants.OFF);
        rule.setPriority(new BigDecimal(102));
        rule.setSectionBegin(Long.valueOf(100)); //100分
        rule.addProductParticipate("15397752");
        rule.addProductParticipate("15397753");
        rule.setRewardList(new ArrayList<RuleReward>() {{
            add(RuleReward.newDiscountReward(3000, 10000, 2));
        }});
        // 测试向一个活动添加一条规则
        newActivity = activityDAO.attachRule(newActivity.getId(), rule);

        List<Rule> ruleList = new ArrayList<Rule>() {{
            Rule rule1 = new Rule();
            rule1.setIssuerId(issuerId);
            rule1.setStatus(Constants.ON);
            rule1.setQualification(Qualify.NOMINAL);
            rule1.setMutex(Constants.OFF);
            rule1.setPriority(new BigDecimal(101));
            rule1.setSectionBegin(Long.valueOf(60000));
            rule1.setSectionEnd(Long.valueOf(90000));
            rule1.setRewardList(new ArrayList<RuleReward>() {{
                add(RuleReward.newProductReward("15413901", 10000, 2));
            }});
            rule1.addProductParticipate("15397752");
            rule1.addProductParticipate("15409950");
            rule1.addProductParticipate("15410050");
            add(rule1);

            Rule rule2 = new Rule();
            rule2.setIssuerId(issuerId);
            rule2.setStatus(Constants.ON);
            rule2.setQualification(Qualify.NOMINAL);
            rule2.setMutex(Constants.OFF);
            rule2.setPriority(new BigDecimal(101));
            rule2.setSectionBegin(Long.valueOf(90000));
            rule2.setRewardList(new ArrayList<RuleReward>() {{
                add(RuleReward.newProductReward("15413900", 10000, 2));
            }});
            add(rule2);
        }};
        // 测试向一个活动添加一组规则
        newActivity = activityDAO.attachRules(newActivity.getId(), ruleList);
        log.info(newActivity);
    }

    // 删除一个活动以及所有规则
    @Test
    public void test_remove_activity() {
        String[] activityIds = new String[] {"0000000141", "0000000142", "0000000143"};
        for (String activityId : activityIds) {
            activityDAO.removeActivity(activityId);
        }
    }

    @Test
    public void test_sync_all_cache() {
        activityDAO.getActivities(issuerId, RewardSituation.BEFORE_MAKE_ORDER);
        activityDAO.getActivities(issuerId, RewardSituation.BEFORE_MAKE_ORDER);
        activityDAO.getActivities(issuerId, RewardSituation.BEFORE_MAKE_ORDER);
        activityDAO.getActivities(issuerId, RewardSituation.AFTER_PAY_SUCCESS);
        activityDAO.getActivities(issuerId, RewardSituation.AFTER_PAY_SUCCESS);

        activityDAO.syncActivities();

        activityDAO.getActivities(issuerId, RewardSituation.BEFORE_MAKE_ORDER);
        activityDAO.getActivities(issuerId, RewardSituation.BEFORE_MAKE_ORDER);
    }

    @Test
    public void test_sync_cache() {
        activityDAO.getActivities(issuerId, RewardSituation.BEFORE_MAKE_ORDER);
        activityDAO.getActivities(issuerId, RewardSituation.BEFORE_MAKE_ORDER);
        activityDAO.getActivities(issuerId, RewardSituation.BEFORE_MAKE_ORDER);
        activityDAO.getActivities(issuerId, RewardSituation.AFTER_PAY_SUCCESS);
        activityDAO.getActivities(issuerId, RewardSituation.AFTER_PAY_SUCCESS);

        activityDAO.syncActivities(issuerId);

        activityDAO.getActivities(issuerId, RewardSituation.BEFORE_MAKE_ORDER);
        activityDAO.getActivities(issuerId, RewardSituation.BEFORE_MAKE_ORDER);
    }

    private Date newDay(int day) {
        Date now = new Date();
        return new Date(now.getTime() + 24 * 3600 * 1000 * day);
    }

    public static void main(String[] args) {
        boolean arraya[] = new boolean[] {true, false};
        boolean arrayb[] = new boolean[] {true, false};
        for (boolean a : arraya) {
            for (boolean b : arrayb) {
                boolean c = a & b;
                boolean d = a && b;
                System.out.println("a:" + a + " b:" + b + " a&b:" + c + " a&&b:" + d);
            }
        }
    }

    // 测试分别保存活动和规则，然后进行关联
    @Deprecated
    @Test
    public void test_activity_save_rule_save_and_finally_attach() {
        String issuerId = "BYJK";
        Activity activity = new Activity();
        activity.setIssuerId(issuerId);
        activity.setTitle("测试互斥活动");
        activity.setRemark("此活动不与其他活动共享");
        activity.setStatus(Constants.ON);
        activity.setValidStart(newDay(-7));
        activity.setValidEnd(newDay(7));
        activity.setMutex(Constants.ON);
        activity.setPriority(new BigDecimal(100));
        Activity newActivity = activityDAO.saveActivityOnly(activity);

        Rule rule1 = new Rule();
        rule1.setIssuerId(issuerId);
        rule1.setStatus(Constants.ON);
        rule1.setQualification(Qualify.NOMINAL);
        rule1.setMutex(Constants.ON);
        rule1.setPriority(new BigDecimal(88));
        rule1.setSectionBegin(Long.valueOf(300));
        rule1.setSectionEnd(Long.valueOf(600));
        rule1.setRewardList(new ArrayList<RuleReward>() {{
            add(RuleReward.newProductReward("15413900", 10000, 1));
        }});
        Rule newRule1 = ruleDAO.saveRule(rule1);

        Rule rule2 = new Rule();
        rule2.setIssuerId(issuerId);
        rule2.setStatus(Constants.ON);
        rule2.setQualification(Qualify.NOMINAL);
        rule2.setMutex(Constants.ON);
        rule2.setPriority(new BigDecimal(90));
        rule2.setSectionBegin(Long.valueOf(600));
        rule2.setRewardList(new ArrayList<RuleReward>() {{
            add(RuleReward.newProductReward("15413901", 10000, 1));
        }});
        Rule newRule2 = ruleDAO.saveRule(rule2);

        ruleParticipationDAO.saveRuleParticipation(newActivity.getId(), newRule1.getId());
        ruleParticipationDAO.saveRuleParticipation(newActivity.getId(), newRule2.getId());
    }

    // 测试删除规则、规则奖励数据、规则商品参与信息、同时也要删除所有引用了该规则的活动参与数据
    @Test
    public void test_detach_rule() {
        String activityId = "0000000090";
        String ruleId = "0000000118";
        ruleDAO.detachRule(activityId, ruleId);
    }
}
