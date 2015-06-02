package com.opvita.activity.daowrapper;

import com.opvita.activity.dto.EsOrderDTO;
import com.opvita.activity.dto.EsOrderItemsDTO;
import com.opvita.activity.dto.EsOrderItemsDTOWithBLOBs;

import java.util.Date;
import java.util.List;

/**
 * Created by rd on 2015/3/27.
 */
public interface EsOrderDAO {

    // 更新订单表
    public EsOrderDTO updateOrder(EsOrderDTO esOrderDTO);

    // 更新订单详情
    public EsOrderItemsDTOWithBLOBs updateOrderItem(EsOrderItemsDTOWithBLOBs item);

    // 获取一定时间内用户购买记录
    public List<EsOrderDTO> getEsOrderList(String custSeq, String issuerId, Date start, Date end);

    // 通过订单id获取订单详情
    public List<EsOrderItemsDTO> getOrderItemList(Integer orderId);
}
