package com.opvita.activity.qualify;

import com.opvita.activity.model.Rule;
import com.opvita.activity.model.EsOrderInfoBean;

import java.math.BigDecimal;

/**
 * Created by rd on 2015/4/27.
 * 计算资格接口
 */
public interface Qualify {

    // 计算资格
    public BigDecimal calculateQualifyValue(EsOrderInfoBean bean, Rule rule);
}
