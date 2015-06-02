package com.opvita.activity.qualify.strategy;

import com.opvita.activity.qualify.calculator.QualifyCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by rd on 2015/6/1.
 */
@Component("countStrategy")
public class CountStrategy extends QualifyStrategy {

    @Autowired @Qualifier("countCalculator")
    private QualifyCalculator calculator;

    @Override
    public QualifyCalculator getCalculator() {
        return calculator;
    }
}
