package com.messiuw.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FormatUtil {

    public static String formatNumberToUseTwoDecimals(String numberAsString) {

        BigDecimal bigDecimal = new BigDecimal(numberAsString);
        bigDecimal = bigDecimal.setScale(2,RoundingMode.HALF_UP);

        return bigDecimal.toString();
    }

}
