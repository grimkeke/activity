package com.opvita.activity.qualify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by rd on 2015/4/27.
 */
@Component
public class QualifyFactory {
    private static NominalQualify nominalQualify;
    private static PaymentQualify paymentQualify;

    public static Qualify newInstance(String ruleQualify) {
        if (Qualify.NOMINAL.equals(ruleQualify)) {
            return QualifyFactory.nominalQualify;

        } else if (Qualify.PAYMENT.equals(ruleQualify)) {
            return QualifyFactory.paymentQualify;

        } else {
            throw new IllegalStateException("invalid qualification:" + ruleQualify);
        }
    }

    @Autowired
    @Qualifier("nominalQualify")
    public void setNominalQualify(NominalQualify nominalQualify) {
        QualifyFactory.nominalQualify = nominalQualify;
    }

    @Autowired
    @Qualifier("paymentQualify")
    public void setPaymentQualify(PaymentQualify paymentQualify) {
        QualifyFactory.paymentQualify = paymentQualify;
    }
}
