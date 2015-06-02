package com.opvita.activity.qualify;

import com.opvita.activity.enums.QualifyType;
import com.opvita.activity.qualify.strategy.CountStrategy;
import com.opvita.activity.qualify.strategy.NominalStrategy;
import com.opvita.activity.qualify.strategy.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by rd on 2015/6/1.
 */
@Component
public class QualifyFactory {
    private static NominalStrategy nominalStrategy;
    private static PaymentStrategy paymentStrategy;
    private static CountStrategy countStrategy;

    public static Qualify newInstance(QualifyType qualifyType) {
        switch (qualifyType) {
            case NOMINAL:
                return nominalStrategy.newOnceStrategy();

            case PAYMENT:
                return paymentStrategy.newOnceStrategy();

            case COUNT:
                return countStrategy.newOnceStrategy();

            case TOTAL_NOMINAL:
                return nominalStrategy.newTotalStrategy();

            case TOTAL_PAYMENT:
                return paymentStrategy.newTotalStrategy();

            case TOTAL_COUNT:
                return countStrategy.newTotalStrategy();

            case SALE_NOMINAL:
                return nominalStrategy.newSaleStrategy();

            case SALE_PAYMENT:
                return paymentStrategy.newSaleStrategy();

            case SALE_COUNT:
                return countStrategy.newSaleStrategy();

            default:
                throw new IllegalStateException("invalid qualify type:" + qualifyType);
        }
    }

    @Autowired @Qualifier("nominalStrategy")
    public void setNominalStrategyFactory(NominalStrategy nominalStrategy) {
        QualifyFactory.nominalStrategy = nominalStrategy;
    }

    @Autowired @Qualifier("paymentStrategy")
    public void setPaymentStrategyFactory(PaymentStrategy paymentStrategy) {
        QualifyFactory.paymentStrategy = paymentStrategy;
    }

    @Autowired @Qualifier("countStrategy")
    public void setCountStrategyFactory(CountStrategy countStrategy) {
        QualifyFactory.countStrategy = countStrategy;
    }
}
