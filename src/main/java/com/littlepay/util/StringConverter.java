package com.littlepay.util;

import com.opencsv.bean.AbstractBeanField;


/**
 * Custom OpenCSV converter for trimming leading and trailing spaces from strings.
 */
public class StringConverter extends AbstractBeanField<String, Integer> {

    /**
     * Converts the input string by trimming leading and trailing spaces.
     *
     * @param s The input string to be converted.
     * @return The input string with leading and trailing spaces removed.
     */
    @Override
    protected String convert(String s) {
        return s.trim();
    }
}