package com.opvita.activity.model;

import com.opvita.activity.utils.ListUtils;
import com.opvita.activity.utils.PriorityComparator;
import com.opvita.activity.common.Constants;
import com.opvita.activity.dto.MActivityDTO;
import com.opvita.activity.model.EsOrderInfoBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Created by rd on 2015/4/25.
 */
public class Activity extends MActivityDTO implements Comparator<Activity>, HavePriority {
    private static Log log = LogFactory.getLog(Activity.class);

    public static final Activity INSTANCE = new Activity();

    // 该活动的规则列表，需要按规则优先级从高到低排序
    private List<Rule> ruleList;

    public void setRuleList(List<Rule> ruleList) {
        this.ruleList = ruleList;
    }

    public void addRule(Rule rule) {
        if (ruleList == null) {
            ruleList = new ArrayList<Rule>();
        }
        ruleList.add(rule);
    }

    public List<Rule> getRuleList() {
        if (ListUtils.isNotEmpty(ruleList)) {
            Collections.sort(ruleList, Rule.INSTANCE);
        }
        return ruleList;
    }

    // 对订单执行活动校验，如果满足活动，则返回所有满足的规则，否则返回null
    public List<Rule> getSatisfiedRules(EsOrderInfoBean bean) {
        List<Rule> satisfiedList = new ArrayList<Rule>();
        List<Rule> rules = getRuleList();

        // 交易发生在活动内，当前时间在活动有效期内
        Date orderDate = bean.getEsOrderDTO().getCreateTime();
        if (isValid()
                && dateValid(orderDate)
                && dateValid(new Date())
                && ListUtils.isNotEmpty(rules)) {
            for (Rule rule : rules) {
                Rule satisfiedRule = rule.checkOrder(bean);
                if (satisfiedRule != null) {
                    satisfiedList.add(satisfiedRule);
                }
            }
        }
        return satisfiedList;
    }

    public boolean isValid() {
        return Constants.ON.equals(getStatus());
    }

    public boolean dateValid(Date orderDate) {
        if (orderDate == null) {
            return false;
        }

        boolean valid = true;
        if (getValidStart() != null) {
            valid &= (getValidStart().compareTo(orderDate) <= 0);
            if (!valid) {
                log.debug("date:" + orderDate + " not valid for start:" + getValidStart());
                return false;
            }
        }

        if (getValidEnd() != null) {
            valid &= (orderDate.compareTo(getValidEnd()) < 0);
        }

        log.debug("orderDate:" + orderDate + " [" + getValidStart() + ", " + getValidEnd() + "] valid activity?" + valid);
        return valid;
    }

    public boolean isMutex() {
        // 表示此活动是否不与其他活动共享
        return Constants.ON.equals(getMutex());
    }

    @Override
    public int compare(Activity o1, Activity o2) {
        // 按优先级降序排列
        return PriorityComparator.descentOrder(o1, o2);
    }

    public MActivityDTO toDTO() {
        MActivityDTO dto = new MActivityDTO();
        dto.setId(getId());
        dto.setTitle(getTitle());
        dto.setRemark(getRemark());
        dto.setIssuerId(getIssuerId());
        dto.setStatus(getStatus());
        dto.setValidStart(getValidStart());
        dto.setValidEnd(getValidEnd());
        dto.setMutex(getMutex());
        dto.setPriority(getPriority());
        dto.setChannel(getChannel());
        dto.setCreateUser(getCreateUser());
        dto.setCreateTimestamp(getCreateTimestamp());
        dto.setUpdateUser(getUpdateUser());
        dto.setUpdateTimestamp(getUpdateTimestamp());
        return dto;
    }

    public static Activity fromDTO(MActivityDTO dto) {
        if (dto == null) {
            return null;
        }

        Activity activity = new Activity();
        activity.setId(dto.getId());
        activity.setTitle(dto.getTitle());
        activity.setRemark(dto.getRemark());
        activity.setIssuerId(dto.getIssuerId());
        activity.setStatus(dto.getStatus());
        activity.setValidStart(dto.getValidStart());
        activity.setValidEnd(dto.getValidEnd());
        activity.setMutex(dto.getMutex());
        activity.setPriority(dto.getPriority());
        activity.setChannel(dto.getChannel());
        activity.setCreateUser(dto.getCreateUser());
        activity.setCreateTimestamp(dto.getCreateTimestamp());
        activity.setUpdateUser(dto.getUpdateUser());
        activity.setUpdateTimestamp(dto.getUpdateTimestamp());
        return activity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Activity{" + getPriority() + "_" + getStatus() + "_" + getMutex() + "_@" + getId());
        sb.append(", issuerId='").append(getIssuerId()).append('\'');
        sb.append(", valid=[" + getValidStart() + ", " + getValidEnd() + ")");
        sb.append(", title='").append(getTitle()).append('\'');
        sb.append(", remark='").append(getRemark()).append('\'');
        sb.append(", channel='").append(getChannel()).append('\'');

        if (ruleList != null) {
            sb.append(", rules=" + ruleList);
            sb.append("]");
        }

        sb.append('}');
        return sb.toString();
    }
}
