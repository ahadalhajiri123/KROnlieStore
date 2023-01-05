package com.unifiedtnc.belview_foods;

import java.text.DecimalFormat;

 class StringConvertingDouble {
    String covertValue(String price){

        DecimalFormat formater = new DecimalFormat("0.00");
        return formater.format(Integer.parseInt(price));
    }
}
