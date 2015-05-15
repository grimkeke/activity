/**
 * 
 */
package com.opvita.activity.model;

import com.opvita.activity.dto.EsOrderDTOWithBLOBs;
import com.opvita.activity.dto.EsOrderItemsDTOWithBLOBs;

import java.util.List;

// 原始代码
public class EsOrderInfoBean {

	private EsOrderDTOWithBLOBs esOrderDTO;
	private List<EsOrderItemsDTOWithBLOBs> esOrderItemsList;
	public EsOrderDTOWithBLOBs getEsOrderDTO() {
		return esOrderDTO;
	}
	public void setEsOrderDTO(EsOrderDTOWithBLOBs esOrderDTO) {
		this.esOrderDTO = esOrderDTO;
	}
	public List<EsOrderItemsDTOWithBLOBs> getEsOrderItemsList() {
		return esOrderItemsList;
	}
	public void setEsOrderItemsList(List<EsOrderItemsDTOWithBLOBs> esOrderItemsList) {
		this.esOrderItemsList = esOrderItemsList;
	}
}
