package com.opvita.activity.enums;

/**
 * Created by rd on 2015/5/31.
 */
public enum QualifyType {
    NOMINAL,          // 按当前订单卡面额计算资格
    PAYMENT,          // 按当前订单支付金额计算资格
    COUNT,            // 按当前订单购卡数量计算资格

    TOTAL_NOMINAL,   // 按活动期间累计订单卡面额计算资格
    TOTAL_PAYMENT,   // 按活动期间累计订单支付金额计算资格
    TOTAL_COUNT,     // 按活动期间累计订单卡数量计算资格

    SALE_NOMINAL,    // 按面额计算资格，满足资格可以换购
    SALE_PAYMENT,    // 按支付金额计算资格，满足资格可以换购
    SALE_COUNT       // 按购买数量计算资格，满足资格可以换购
}
