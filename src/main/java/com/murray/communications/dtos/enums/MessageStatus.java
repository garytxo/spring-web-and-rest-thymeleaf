package com.murray.communications.dtos.enums;

/**
 * Message , sms or email, actual status
 */
public enum MessageStatus {
    /**
     * Message saved in database
     */
    CREATED,
    /**
     * Message send to bandwidth for processing. Bandwidth will then hit the webhook to
     * update the status.
     */
    PROCESSING,
    /**
     * When message successfully sent from bandwidth to the recipient
     * This is the result of the call back url
     */
    SENT_SUCCESS,

    /**
     * If issue occurred when bandwidth we attempting to send sms.
     * Again this is related to the callback results.
     */
    SENT_ERROR,
    /**
     * Interal error occurred
     */
    ERROR,

    /**
     * Soft deleted message from system
     */
    DELETED;
}
