package com.littlepay.util;

import com.opencsv.bean.AbstractBeanField;


/**
 * Custom OpenCSV converter for converting strings to TapType constants.
 */
public class TapTypeConverter extends AbstractBeanField<Constants.TAP_TYPE, Integer> {

    /**
     * Converts the input string to a TapType constant by trimming, converting to uppercase, and using valueOf.
     *
     * @param s The input string to be converted.
     * @return The corresponding TapType constant.
     */
    @Override
    protected Constants.TAP_TYPE convert(String s) {
        return Constants.TAP_TYPE.valueOf(s.trim().toUpperCase());
    }
}