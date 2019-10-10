package com.murray.communications.adapters.rest;

import com.murray.communications.dtos.rest.SmsSearchDto;
import com.murray.communications.security.jwtaudit.AuditingCredentials;
import com.murray.communications.dtos.rest.SmsDto;

import java.util.List;

public interface MessageAdapterService {

    /**
     * Save a new @{@link SmsDto}
     *
     * @return {@link SmsDto}
     */
    SmsDto save(AuditingCredentials credentials, SmsDto smsDto);

    /**
     * Find sms by id
     *
     * @param id
     * @return
     */
    SmsDto find(Long id);

    /**
     * Update certain fields of a  @{@link SmsDto}
     *
     * @return {@link SmsDto}
     */
    SmsDto update(AuditingCredentials credentials, SmsDto smsDto);


    /**
     * Send SMS message to
     * @param credentials
     * @param id
     */
    void sendSms(AuditingCredentials credentials , Long id);

    /**
     * Soft delete the sms message
     *
     * @param smsId
     */
    void delete(AuditingCredentials credentials,Long smsId);


    /**
     * Search for sms's using the optional {@link SmsSearchDto}
     *
     * @param smsSearch {@link SmsSearchDto}
     * @return list  {@link SmsDto}
     */
    List<SmsDto> findBy(SmsSearchDto smsSearch);
}
