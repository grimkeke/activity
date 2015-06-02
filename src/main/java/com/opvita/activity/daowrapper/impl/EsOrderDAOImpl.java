package com.opvita.activity.daowrapper.impl;

import com.opvita.activity.dao.EsOrderDTOMapper;
import com.opvita.activity.dao.EsOrderItemsDTOMapper;
import com.opvita.activity.daowrapper.EsOrderDAO;
import com.opvita.activity.dto.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by rd on 2015/3/27.
 */
@Service
public class EsOrderDAOImpl implements EsOrderDAO {
    private static Log log = LogFactory.getLog(EsOrderDAOImpl.class);

    @Autowired
    private EsOrderDTOMapper esOrderDTOMapper;
    @Autowired
    private EsOrderItemsDTOMapper esOrderItemsDTOMapper;


    @Override
    public EsOrderDTO updateOrder(EsOrderDTO esOrderDTO) {
        esOrderDTOMapper.updateByPrimaryKey(esOrderDTO);
        return esOrderDTO;
    }

    @Override
    public EsOrderItemsDTOWithBLOBs updateOrderItem(EsOrderItemsDTOWithBLOBs item) {
        esOrderItemsDTOMapper.updateByPrimaryKey(item);
        return item;
    }

    @Override
    public List<EsOrderDTO> getEsOrderList(String custSeq, String issuerId, Date start, Date end) {
        if (StringUtils.isEmpty(custSeq)) {
            throw new NullPointerException("custSeq should not be null!");
        }

        if (StringUtils.isEmpty(issuerId)) {
            throw new NullPointerException("issuerId should not be null!");
        }

        EsOrderDTOCriteria orderDTOCriteria = new EsOrderDTOCriteria();
        EsOrderDTOCriteria.Criteria criteria = orderDTOCriteria.createCriteria();
        criteria.andCustSeqEqualTo(custSeq)
                .andIssuerIdEqualTo(issuerId)
                .andPayStatusEqualTo("1")    // 支付成功
                .andStatusEqualTo("1");      // 状态正常

        if (start != null) {
            criteria.andCreateTimeGreaterThanOrEqualTo(start);
        }

        if (end != null) {
            criteria.andCreateTimeLessThanOrEqualTo(end);
        }

        return esOrderDTOMapper.selectByExample(orderDTOCriteria);
    }

    @Override
    public List<EsOrderItemsDTO> getOrderItemList(Integer orderId) {
        EsOrderItemsDTOCriteria criteria = new EsOrderItemsDTOCriteria();
        criteria.createCriteria().andOrderIdEqualTo(orderId);
        return esOrderItemsDTOMapper.selectByExample(criteria);
    }
}
