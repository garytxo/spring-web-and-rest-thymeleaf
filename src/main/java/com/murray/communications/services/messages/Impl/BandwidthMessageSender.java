package com.murray.communications.services.messages.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murray.communications.domain.entities.messages.SmsMessage;
import com.murray.communications.dtos.enums.MessageStatus;
import com.murray.communications.domain.entities.messages.BandWidthCredentials;
import com.murray.communications.exceptions.BandWidthException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;


/**
 * Responsible for building the message HTTP request and handling the response
 */
@Slf4j
class BandwidthMessageSender {

    static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    static final String HTTPS_MESSAGING_BANDWIDTH_COM_API_V_2_USERS_S_MESSAGES = "https://messaging.bandwidth.com/api/v2/users/%s/messages";
    private final ObjectMapper objectMapper;

    BandwidthMessageSender() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Send {@link SmsMessage} to bandwidth messaging service for handling.
     *
     * @param smsMessage
     * @return
     */
    public int send(final SmsMessage smsMessage) throws BandWidthException {


        if (!smsMessage.getStatus().equals(MessageStatus.CREATED) || smsMessage.isDeleted()
                || smsMessage.getSendDate().isBefore(LocalDate.now())) {
            log.warn("Skip sending message:{} because status:{}", smsMessage.getId(), smsMessage.getStatus());
            throw new BandWidthException(String.format("Message %s not valid for processing ",smsMessage.getId()));
        }

        try {

            OkHttpClient client = new OkHttpClient();

            Request request = buildPost(smsMessage);

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                log.error("Error sending message:{} code:{} response:{}", smsMessage.getId(), response.code(), response.body().string());
                throw new BandWidthException("Error sending message", response.code(), response.body().string());
            }
            log.info("message successfully processed code:{}, response:{}", response.code(), response.body().string());
            return response.code();

        } catch (IOException e) {
            log.error("Issue sending message:{}", smsMessage.getId(), e);
            throw new BandWidthException("Error sending message", e);
        }

    }

    /**
     * Build http post request using the message and badwidth credientals
     *
     * @param smsMessage
     * @return Request
     * @throws JsonProcessingException
     */
    private Request buildPost(final SmsMessage smsMessage) throws JsonProcessingException {

        RequestBody body = RequestBody.create(JSON, toJson(smsMessage));

        String authorization = getAuthorization(smsMessage.getBandWidthCredentials().getMessageApi(), smsMessage.getBandWidthCredentials().getMessageSecret());

        Request request = new Request.Builder()
                .url(toUrl(smsMessage.getBandWidthCredentials()))
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", authorization)
                .addHeader("Accept", "*/*")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Host", "messaging.bandwidth.com")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Content-Length", "200")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();

        return request;

    }

    /**
     * Generate the basic authorisation header value used in the http header
     *
     * @param token
     * @param secret
     * @return
     */
    private String getAuthorization(String token, String secret) {
        return Credentials.basic(token, secret);

    }


    /**
     * Build the bandwidth url that is used for posting message
     *
     * @param bandWidthCredentials
     * @return
     */
    private String toUrl(BandWidthCredentials bandWidthCredentials) {

        return String.format(HTTPS_MESSAGING_BANDWIDTH_COM_API_V_2_USERS_S_MESSAGES, bandWidthCredentials.getDashboardAccountId());
    }

    /**
     * Converts the {@link SmsMessage} into json form that can be supported by bandwidth
     *
     * @param smsMessage
     * @return
     * @throws JsonProcessingException
     */
    private String toJson(SmsMessage smsMessage) throws JsonProcessingException {

        Map<String, Object> payload = new Hashtable<>();
        payload.put("to", Arrays.asList("+" + smsMessage.getReceiver()));
        payload.put("from", Long.toString(smsMessage.getBandWidthCredentials().getSenderPhoneNumber()));
        payload.put("text", smsMessage.getMessage());
        payload.put("tag", smsMessage.getId());
        payload.put("applicationId", smsMessage.getBandWidthCredentials().getApplicationId());

        return objectMapper.writeValueAsString(payload);
    }


}
