package com.littlepay.util;

import com.opencsv.bean.BeanField;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * Custom OpenCSV mapping strategy for generating headers with CsvBindByName annotations.
 *
 * @param <T> The type of the bean.
 * @see <a href="https://stackoverflow.com/questions/51768071/how-to-generate-header-in-opencsv">Stack Overflow</a>
 */
public class CustomMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {

    /**
     * Generates the header using the CsvBindByName annotations from the bean class.
     *
     * @param bean The bean object to generate the header for.
     * @return An array containing the generated header column names.
     * @throws CsvRequiredFieldEmptyException If a required field is found to be empty.
     */
    @Override
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
        final int numColumns = getFieldMap().values().size();
        super.generateHeader(bean);

        String[] header = new String[numColumns];

        BeanField<T, Integer> beanField;
        for (int i = 0; i < numColumns; i++) {
            beanField = findField(i);
            String columnHeaderName = extractHeaderName(beanField);
            header[i] = columnHeaderName;
        }

        return header;
    }

    /**
     * Extracts the header name from the CsvBindByName annotation.
     *
     * @param beanField The bean field containing the annotation.
     * @return The extracted header name, or an empty string if not found.
     */
    private String extractHeaderName(final BeanField<T, Integer> beanField) {
        if (Optional.ofNullable(beanField).isEmpty() || Optional.ofNullable(beanField.getField()) .isEmpty() || beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class).length == 0) {
            return StringUtils.EMPTY;
        }

        final CsvBindByName bindByNameAnnotation = beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class)[0];
        return bindByNameAnnotation.column();
    }
}




