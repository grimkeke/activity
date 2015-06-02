package com.opvita.activity.model;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by rd on 2015/5/31.
 */
public class SaleProductInfo {
    private Set<String> saleProductSet = new HashSet<String>();
    private Map<String, SaleProduct> saleProductMap = new HashMap<String, SaleProduct>();

    public boolean isSaleProduct(String productId) {
        return saleProductSet.contains(productId);
    }

    public boolean isNotSaleProduct(String productId) {
        return !isSaleProduct(productId);
    }

    public SaleProductInfo addSaleProduct(SaleProduct saleProduct) {
        String productId = saleProduct.getProductId();
        if (StringUtils.isEmpty(productId)) {
            throw new IllegalStateException("invalid saleProduct " + saleProduct);
        }

        if (saleProductSet.contains(productId) &&
                saleProductMap.get(productId) != null) {
            throw new IllegalStateException("SaleProductInfo contain duplicate productId");
        }

        saleProductSet.add(productId);
        saleProductMap.put(productId, saleProduct);
        return this;
    }

    public SaleProduct getSaleProduct(String productId) {
        return saleProductMap.get(productId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SaleProductInfo{");
        sb.append("saleProductSet=").append(saleProductSet);
        sb.append(", saleProductMap=").append(saleProductMap);
        sb.append('}');
        return sb.toString();
    }
}
