package com.murray.communications.adapters.web;

import com.murray.communications.dtos.web.NewSMSMessageDto;
import com.murray.communications.dtos.web.SMSMessageDto;
import com.murray.communications.dtos.web.SmsSearchResults;

/**
 * Manages the Sms web calls that use the REST and SMS Service
 */
public interface SmsMessageAdapter {


    /**
     * find all message for a specific page
     * @param page actual page of results
     * @param size number of elements to returm
     * @return SmsSearchResults
     */
    SmsSearchResults findAllMessages(Integer page, Integer size);

    /**
     * Calls the REST API to get the JWT token for the current user logged in
     *
     * @return
     */
    String getTokenForLoggedUser();


    /**
     * Save sms
     *
     * @param smsDto
     * @return
     */
    SMSMessageDto saveSms(NewSMSMessageDto smsDto);


    /**
     * find the sms message by their unquie id
     *
     * @param id
     * @return SMSMessageDto
     */
    SMSMessageDto findMessageBy(Long id);


    /**
     * Update the sms certain fields
     *
     * @param dto
     * @return
     */
    SMSMessageDto update(SMSMessageDto dto);

    /**
     * Delete sms by id
     * @param id
     */
    void deleteById(Long id);


    /**
     * Delete sms by id
     * @param id
     */
    void sendMessage(Long id);

}
