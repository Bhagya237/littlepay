package com.littlepay.util;

import com.opencsv.bean.AbstractBeanField;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * Custom OpenCsv converter for converting strings to DateTime objects and vice versa.
 */
public class DateTimeConverter extends AbstractBeanField<DateTime, Integer> {

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-YYYY HH:mm:ss");

    /**
     * Converts the input string to a DateTime object by trimming and parsing with the dd-MM-YYYY HH:mm:ss format.
     *
     * @param s The input string to be converted.
     * @return The corresponding DateTime object.
     */
    @Override
    protected DateTime convert(String s) {
        return DateTime.parse(s.trim(), formatter);
    }

    /**
     * Converts a DateTime object to a formatted dd-MM-YYYY HH:mm:ss string for writing.
     *
     * @param value The DateTime object to be converted.
     * @return The formatted string representation.
     */
    @Override
    protected String convertToWrite(Object value) {

        // Return empty string if value is null
        if(value == null){
            return "";
        }
        return formatter.print((DateTime) value);
    }
}