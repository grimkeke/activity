package com.opvita.activity.daowrapper;

import com.opvita.activity.model.SaleProduct;
import com.opvita.activity.model.SaleProductInfo;

import java.util.List;

/**
 * Created by rd on 2015/5/31.
 */
public interface SaleProductDAO {

    // 保存一条换购商品记录
    public SaleProduct saveSaleProduct(SaleProduct saleProduct);

    // 获取换购商品列表
    public List<SaleProduct> getSaleProductList(String issuerId);
    public List<SaleProduct> getSaleProductList(String issuerId, String ruleId);

    public SaleProductInfo getSaleProductInfo(String issuerId, String ruleId);
}
