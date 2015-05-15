package com.opvita.activity.model;

import com.opvita.activity.common.Constants;
import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTOWithBLOBs;
import com.opvita.activity.dto.MActivityRuleDTO;
import com.opvita.activity.qualify.Qualify;
import com.opvita.activity.qualify.QualifyFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rd on 2015/4/25.
 */
public class Rule extends MActivityRuleDTO implements Comparator<Rule> {
    private static Log log = LogFactory.getLog(Rule.class);

    private boolean satisfied;
    private boolean supportAll;
    private Set<String> productSet;  // 哪些商品参与此规则
    private List<RuleReward> rewardList;  // 一条规则对应的奖励数据列表，

    // 规则资格
    private Qualify qualify;

    public Rule() {
        this.satisfied = false;
        this.supportAll = true;
    }

    public boolean isValid() {
        return Constants.ON.equals(getStatus());
    }

    public boolean isMutex() {
        // 表示此规则是否不与其他规则共享
        return Constants.ON.equals(getMutex());
    }

    public boolean isSatisfied() {
        return satisfied;
    }

    public List<RuleReward> getRewardList() {
        return rewardList;
    }

    public void setRewardList(List<RuleReward> rewardList) {
        this.rewardList = rewardList;
    }

    public Set<String> getProductSet() {
        return productSet;
    }

    public void addProductParticipate(String productId) {
        if (productSet == null) {
            productSet = new HashSet<String>();
        }

        if (StringUtils.isEmpty(productId)) {
            if (supportAll == false) {
                throw new IllegalStateException("此规则需要指定商品参与，不能再添加空商品号.actor:" + this);
            }

        } else {
            // 使用set，可以防止相同商品重复提交，但无法校验重复出现的商品号，也无法校验重复出现的全空商品号记录
            productSet.add(productId);
            supportAll = false;
        }
    }

    // 判断该商品号是否参与此规则
    public boolean productParticipate(String productId) {
        return productSet != null && productSet.contains(productId);
    }

    public boolean supportAllProducts() {
        return supportAll;
    }

    // 对订单执行规则校验，如果满足规则，则返回规则数据，不满足则返回null
    public Rule checkOrder(EsOrderInfoBean bean) {
        if (isValid()) {
            if (StringUtils.isNotEmpty(getPayType())) {
                // todo 根据支付类型
            }

            // 根据资格和商品参与规则计算订单资格数值
            BigDecimal orderValue = calculateOrderValue(bean);

            satisfied = orderValueSatisfy(orderValue);
            log.info("satisfied:[" + satisfied + "] order:" + bean.getEsOrderDTO().getSn() + " value:" + orderValue  + " " + this);

            if (satisfied) {
                return this;
            }
        }
        return null;
    }

    // 金额是否满足规则数据区间
    public boolean orderValueSatisfy(BigDecimal orderValue) {
        boolean satisfy = true; // sectionBegin and sectionEnd both null, 说明无限制，均满足
        if (getSectionBegin() != null) {
            satisfy = (new BigDecimal(getSectionBegin()).compareTo(orderValue) <= 0);
        }

        if (getSectionEnd() != null) {
            satisfy &= (orderValue.compareTo(new BigDecimal(getSectionEnd())) < 0);
        }

        log.debug("orderValue:" + orderValue + " section:[" + getSectionBegin() + ", " +
                getSectionEnd() + ") satisfy:" + satisfy);
        return satisfy;
    }

    public BigDecimal calculateOrderValue(EsOrderInfoBean bean) {
        EsOrderDTO esOrder = bean.getEsOrderDTO();
        List<EsOrderItemsDTOWithBLOBs> orderItemList = bean.getEsOrderItemsList();

        BigDecimal orderValue = new BigDecimal(0);
        if (supportAllProducts()) {
            orderValue = qualify.qualifyOrderValue(esOrder);
            log.debug("order value:" + orderValue + " for order:" + esOrder.getSn());

        } else {
            for (EsOrderItemsDTOWithBLOBs orderItem : orderItemList) {
                String productId = String.valueOf(orderItem.getProductId());
                if (productParticipate(productId)) {
                    BigDecimal itemValue = qualify.qualifyOrderItemValue(orderItem);
                    orderValue = orderValue.add(itemValue);
                }
            }
            log.debug("order value:" + orderValue + " for order:" + esOrder.getSn() + " set:" + getProductSet());
        }
        return orderValue;
    }

    @Override
    public int compare(Rule o1, Rule o2) {
        // 按优先级降序排列
        return o2.getPriority().subtract(o1.getPriority()).intValue();
    }

    public MActivityRuleDTO toDTO() {
        MActivityRuleDTO dto = new MActivityRuleDTO();
        dto.setId(getId());
        dto.setIssuerId(getIssuerId());
        dto.setStatus(getStatus());
        dto.setQualification(getQualification());
        dto.setPayType(getPayType());
        dto.setMutex(getMutex());
        dto.setPriority(getPriority());
        dto.setSectionBegin(getSectionBegin());
        dto.setSectionEnd(getSectionEnd());
        dto.setCreateUser(getCreateUser());
        dto.setCreateTimestamp(getCreateTimestamp());
        dto.setUpdateUser(getUpdateUser());
        dto.setUpdateTimestamp(getUpdateTimestamp());
        return dto;
    }

    public static Rule fromDTO(MActivityRuleDTO dto) {
        if (dto == null) {
            return null;
        }

        Rule rule = new Rule();
        rule.setId(dto.getId());
        rule.setIssuerId(dto.getIssuerId());
        rule.setStatus(dto.getStatus());
        rule.setQualification(dto.getQualification());
        rule.setPayType(dto.getPayType());
        rule.setMutex(dto.getMutex());
        rule.setPriority(dto.getPriority());
        rule.setSectionBegin(dto.getSectionBegin());
        rule.setSectionEnd(dto.getSectionEnd());
        rule.setCreateUser(dto.getCreateUser());
        rule.setCreateTimestamp(dto.getCreateTimestamp());
        rule.setUpdateUser(dto.getUpdateUser());
        rule.setUpdateTimestamp(dto.getUpdateTimestamp());
        rule.qualify = QualifyFactory.newInstance(rule.getQualification());
        return rule;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Rule{" + getPriority() + "_" + getStatus() + "_" + getMutex() + "_@" + getId());
        sb.append(", issuerId='").append(getIssuerId()).append('\'');
        sb.append(", qualification='").append(getQualification()).append('\'');
        sb.append(", payType='").append(getPayType()).append('\'');
        sb.append(", supportAll=").append(supportAll);
        sb.append(", section=[" + getSectionBegin() + ", " + getSectionEnd() + ")");

        if (rewardList != null) {
            sb.append(", rewardList=");
            sb.append(rewardList);
        }

        if (productSet != null) {
            sb.append(", productSet=" + productSet);
        }

        sb.append("}");
        return sb.toString();
    }
}
