package com.opvita.activity.model;

import com.opvita.activity.common.Constants;
import com.opvita.activity.dto.MSaleProductDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by rd on 2015/5/31.
 */
public class SaleProduct extends MSaleProductDTO {
    private static Log log = LogFactory.getLog(SaleProduct.class);

    public static SaleProduct newInstance(String productId, Long salePrice, String ruleId, String issuerId) {
        SaleProduct saleProduct = new SaleProduct();
        saleProduct.setProductId(productId);
        saleProduct.setSalePrice(salePrice);
        saleProduct.setRuleId(ruleId);
        saleProduct.setIssuerId(issuerId);
        saleProduct.setStatus(Constants.ON);
        return saleProduct;
    }

    public MSaleProductDTO toDTO() {
        MSaleProductDTO dto = new MSaleProductDTO();
        dto.setId(getId());
        dto.setProductId(getProductId());
        dto.setSalePrice(getSalePrice());
        dto.setRuleId(getRuleId());
        dto.setStatus(getStatus());
        dto.setIssuerId(getIssuerId());
        dto.setCreateUser(getCreateUser());
        dto.setCreateTimestamp(getCreateTimestamp());
        dto.setUpdateUser(getUpdateUser());
        dto.setUpdateTimestamp(getUpdateTimestamp());
        return dto;
    }

    public static SaleProduct fromDTO(MSaleProductDTO dto) {
        if (dto == null) {
            return null;
        }

        SaleProduct saleProduct = new SaleProduct();
        saleProduct.setId(dto.getId());
        saleProduct.setProductId(dto.getProductId());
        saleProduct.setSalePrice(dto.getSalePrice());
        saleProduct.setRuleId(dto.getRuleId());
        saleProduct.setStatus(dto.getStatus());
        saleProduct.setIssuerId(dto.getIssuerId());
        saleProduct.setCreateUser(dto.getCreateUser());
        saleProduct.setCreateTimestamp(dto.getCreateTimestamp());
        saleProduct.setUpdateUser(dto.getUpdateUser());
        saleProduct.setUpdateTimestamp(dto.getUpdateTimestamp());
        return saleProduct;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SaleProduct{" + getStatus() + "@" + getId());
        sb.append(", productId='").append(getProductId()).append('\'');
        sb.append(", salePrice=").append(getSalePrice());
        sb.append(", ruleId='").append(getRuleId()).append('\'');
        sb.append(", issuerId='").append(getIssuerId()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
