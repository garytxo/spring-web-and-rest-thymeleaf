package com.murray.communications.services.formatters.impl;

import com.murray.communications.services.formatters.CustomDateFormatter;

import java.time.format.DateTimeFormatter;

/**
 * {@inheritDoc}
 */
public class CustomDateFormatterImpl implements CustomDateFormatter {


    private DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * {@inheritDoc}
     */
    @Override
    public DateTimeFormatter localDateFormatter() {

        return localDateFormatter;
    }
}
