package com.opvita.activity.enums;

/**
 * Created by rd on 2015/5/31.
 */
public enum RewardType {
    NEW_CARD,           // 新卡奖励，送指定卡片
    EXTRA_CARD,         // 送购买卡类型一致的卡片（即实现类似的买三送一）
    DISCOUNT,           // 数值折扣
    PERCENTAGE,         // 百分比折扣
    RECHARGE_PCARD,     // 充值主卡
    CASHIER_RECHARGE,   // 操作员主卡充值返利
    SALE                 // 满额换购
}
