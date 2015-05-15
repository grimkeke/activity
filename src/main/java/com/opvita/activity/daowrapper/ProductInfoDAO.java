package com.opvita.activity.daowrapper;

/**
 * Created by rd on 2015/5/15.
 */
public interface ProductInfoDAO {
    // 通过商品id查询卡金额（单位为分）
    public Long getProductCardPrice(String productId);
}
