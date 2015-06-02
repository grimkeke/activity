package com.opvita.activity.daowrapper.impl;

import com.opvita.activity.model.SaleProduct;
import com.opvita.activity.model.SaleProductInfo;
import com.opvita.activity.utils.ListUtils;
import com.opvita.activity.common.Constants;
import com.opvita.activity.dao.ActivityMapper;
import com.opvita.activity.dao.MSaleProductDTOMapper;
import com.opvita.activity.daowrapper.SaleProductDAO;
import com.opvita.activity.dto.MSaleProductDTO;
import com.opvita.activity.dto.MSaleProductDTOCriteria;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rd on 2015/5/31.
 */
@Service
public class SaleProductDAOImpl implements SaleProductDAO {
    @Autowired private MSaleProductDTOMapper mapper;
    @Autowired private ActivityMapper activityMapper;

    @Override
    public SaleProduct saveSaleProduct(SaleProduct saleProduct) {
        saleProduct.setId(activityMapper.nextSaleProductId());
        saleProduct.setCreateUser("ROOT");
        saleProduct.setCreateTimestamp(new Date());
        mapper.insert(saleProduct.toDTO());
        return saleProduct;
    }

    @Override
    public List<SaleProduct> getSaleProductList(String issuerId) {
        return getSaleProductList(issuerId, null);
    }

    @Override
    public List<SaleProduct> getSaleProductList(String issuerId, String ruleId) {
        MSaleProductDTOCriteria saleProductDTOCriteria = new MSaleProductDTOCriteria();
        MSaleProductDTOCriteria.Criteria criteria = saleProductDTOCriteria.createCriteria();
        criteria.andIssuerIdEqualTo(issuerId).andStatusEqualTo(Constants.ON);

        if (StringUtils.isNotEmpty(ruleId)) {
            criteria.andRuleIdEqualTo(ruleId);
        }

        List<SaleProduct> saleProductList = null;
        List<MSaleProductDTO> dtoList = mapper.selectByExample(saleProductDTOCriteria);
        if (ListUtils.isNotEmpty(dtoList)) {
            saleProductList = new ArrayList<SaleProduct>(dtoList.size());

            for (MSaleProductDTO dto : dtoList) {
                SaleProduct saleProduct = SaleProduct.fromDTO(dto);
                saleProductList.add(saleProduct);
            }
        }
        return saleProductList;
    }

    @Override
    public SaleProductInfo getSaleProductInfo(String issuerId, String ruleId) {
        SaleProductInfo info = new SaleProductInfo();
        List<SaleProduct> saleProductList = getSaleProductList(issuerId, ruleId);
        if (ListUtils.isNotEmpty(saleProductList)) {
            for (SaleProduct saleProduct : saleProductList) {
                info.addSaleProduct(saleProduct);
            }
        }
        return info;
    }
}
