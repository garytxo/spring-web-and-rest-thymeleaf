package com.murray.communications.services.formatters;

import java.time.format.DateTimeFormatter;

/**
 * Handle all internal date formatting to covert from string to java.time.*
 */
public interface CustomDateFormatter {


    /**
     * Return the {@link DateTimeFormatter} used for formatting
     * {@link java.time.LocalDate}
     *
     * @return {@link DateTimeFormatter}
     */
    DateTimeFormatter localDateFormatter();
}
