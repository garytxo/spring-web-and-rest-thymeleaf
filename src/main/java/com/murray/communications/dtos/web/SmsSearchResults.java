package com.murray.communications.dtos.web;

import lombok.Data;

import java.util.List;

/**
 * Represents the Sms search results returned from the repo.
 */
@Data
public class SmsSearchResults {

    /**
     * Total number of pages
     */
    private Integer totalPages;

    /**
     * Actual page number
     */
    private Integer pageNumber;

    /**
     * Total number message found
     */
    private Long totalElements;

    /**
     * Messages found
     */
    private List<SMSMessageDto> messages;
}
