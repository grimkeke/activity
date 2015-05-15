package com.opvita.activity.daowrapper.impl;

import com.opvita.activity.dao.EsOrderDTOMapper;
import com.opvita.activity.daowrapper.EsOrderDAO;
import com.opvita.activity.dto.EsOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rd on 2015/3/27.
 */
@Service
public class EsOrderDAOImpl implements EsOrderDAO {

    @Autowired
    private EsOrderDTOMapper esOrderDTOMapper;

    @Override
    public EsOrderDTO updateOrder(EsOrderDTO esOrderDTO) {
        esOrderDTOMapper.updateByPrimaryKey(esOrderDTO);
        return esOrderDTO;
    }
}
