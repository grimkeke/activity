package com.opvita.activity.qualify.strategy;

import com.opvita.activity.qualify.OnceQualify;
import com.opvita.activity.qualify.SaleQualify;
import com.opvita.activity.qualify.TotalQualify;
import com.opvita.activity.qualify.calculator.QualifyCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by rd on 2015/6/1.
 */
public abstract class QualifyStrategy {
    @Autowired @Qualifier("onceQualify")
    private OnceQualify onceQualify;

    @Autowired @Qualifier("totalQualify")
    private TotalQualify totalQualify;

    @Autowired @Qualifier("saleQualify")
    private SaleQualify saleQualify;

    public abstract QualifyCalculator getCalculator();

    public OnceQualify newOnceStrategy() {
        onceQualify.setCalculator(getCalculator());
        return onceQualify;
    }

    public TotalQualify newTotalStrategy() {
        totalQualify.setCalculator(getCalculator());
        return totalQualify;
    }

    public SaleQualify newSaleStrategy() {
        saleQualify.setCalculator(getCalculator());
        return saleQualify;
    }
}
