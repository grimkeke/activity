package com.opvita.activity.daowrapper.impl;

import com.opvita.activity.dao.MProductInfoDTOMapper;
import com.opvita.activity.daowrapper.ProductInfoDAO;
import com.opvita.activity.dto.MProductInfoDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rd on 2015/5/15.
 */
@Service
public class ProductInfoDAOImpl implements ProductInfoDAO {
    @Autowired
    private MProductInfoDTOMapper mapper;

    @Override
    public Long getProductCardPrice(String productId) {
        if (StringUtils.isEmpty(productId)) {
            throw new NullPointerException("productId should not be null!");
        }

        MProductInfoDTO productInfo = mapper.selectByPrimaryKey(productId);
        if (productInfo == null) {
            return null;
        }

        return productInfo.getCardPrice();
    }
}
