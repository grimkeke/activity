package com.opvita.activity.qualify;

import com.opvita.activity.qualify.calculator.QualifyCalculator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by rd on 2015/5/31.
 * 计算资格算法的抽象类，通过设置不同的资格计算器，实现资格计算的方式
 * 包含计算资格的类型（当次资格、累积资格、换购资格）和计算资格的方法（参见QualifyCalculator）
 */
public abstract class AbsQualify implements Qualify {
    protected Log log = LogFactory.getLog(getClass());

    // 计算资格的接口，通过设置不同的计算资格方式来计算累计资格
    private QualifyCalculator calculator;

    public void setCalculator(QualifyCalculator calculator) {
        this.calculator = calculator;
    }

    public QualifyCalculator getCalculator() {
        return calculator;
    }
}
