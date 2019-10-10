package com.murray.communications.services.messages;

import com.murray.communications.security.jwtaudit.AuditingCredentials;
import com.murray.communications.domain.entities.messages.SmsMessage;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public interface MessageService {


    /**
     * Create a new SMS message that will be sent using the sender number to the receiver number
     * and will be sent on the sendOn date.
     *
     * @param credentials user details that are creating the
     * @param receiver    integer receiver number to receive the message
     * @param message     string message
     * @param sendOn      {@link ZonedDateTime} when the message should be sent
     * @return SmsMessage
     */
    SmsMessage createSms(final AuditingCredentials credentials, final Long receiver, final String message, final LocalDate sendOn);


    /**
     * find {@link SmsMessage} by long
     *
     * @param id
     * @return
     */
    SmsMessage findBy(Long id);

    /**
     * Update sms message detals
     *
     * @param credentials user details that are creating the
     * @param smsMessage
     * @return
     */
    SmsMessage update(AuditingCredentials credentials, SmsMessage smsMessage);

    /**
     * Delete messsage by id.
     *
     * @param id
     */
    void deleteMessageBy(AuditingCredentials credentials,Long id);



    /**
     * Send SMS message to external source for sending
     * @param credentials
     * @param id
     */
    void sendSms(AuditingCredentials credentials , Long id);


}
