package com.opvita.activity.daowrapper;


import com.opvita.activity.dto.MProductParticipationDTO;

import java.util.List;
import java.util.Set;

/**
 * Created by rd on 2015/4/27.
 */
public interface ProductParticipationDAO {
    // 保存商品参与规则
    public List<MProductParticipationDTO> saveProductParticipation(String ruleId, Set<String> productSet);

    // 获取一个规则下的所有商品参与信息
    public List<MProductParticipationDTO> getProductParticipation(String ruleId);

    // 物理删除一个规则的所有商品参与规则
    // 推荐使用软删除
    public int clearProductParticipation(String ruleId);

    // 软删除一个规则的所有商品参与规则
    public int invalidateProductParticipation(String ruleId);
}
