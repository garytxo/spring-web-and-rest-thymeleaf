package com.murray.communications.services.messages.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murray.communications.domain.entities.messages.SmsMessage;
import com.murray.communications.dtos.enums.MessageStatus;
import com.murray.communications.domain.entities.messages.BandWidthCredentials;
import com.murray.communications.exceptions.BandWidthException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class MessageSenderTest {

    private ObjectMapper objectMapper;
    private BandwidthMessageSender sender;

    private SmsMessage message;

    @Before
    public void setup() {

        message = new SmsMessage();
        message.setId(11212L);
        message.setStatus(MessageStatus.CREATED);
        message.setMessage("unit test hello");
        message.setSender(5012141397L);
        message.setReceiver(34680522200L);
        message.setDeleted(false);
        message.setSendDate(LocalDate.now().plusDays(1));

        BandWidthCredentials bandWidthCredentials = new BandWidthCredentials();
        bandWidthCredentials.setId(11L);
        bandWidthCredentials.setMessageApi("83baba800d9cf9e1e8811fc527ee7aaea0f03a671265ce2a");
        bandWidthCredentials.setMessageSecret("fe537df3e6c6f1433247775147014fe3e91dab34d1a471a7");
        bandWidthCredentials.setApplicationName("test-location-application");
        bandWidthCredentials.setSenderPhoneNumber(5012141397L);
        bandWidthCredentials.setApplicationId("e5bca7db-1158-40a3-8b74-35f846e8b549");
        bandWidthCredentials.setDashboardUserName("gfinchdev");
        bandWidthCredentials.setDashboardPwd("G!1juf5T");
        bandWidthCredentials.setDashboardAccountId("5004987");
        bandWidthCredentials.setDashboardSubAccountId("24402");

        message.setBandWidthCredentials(bandWidthCredentials);

        objectMapper = new ObjectMapper();
        sender = new BandwidthMessageSender();

    }

    @Test(expected = BandWidthException.class)
    public void not_send_message_when_status_is_not_created() throws Exception {

        message.setStatus(MessageStatus.SENT_SUCCESS);
        int result = sender.send(message);
        assertThat(result, is(equalTo(-1)));
    }

    @Test(expected = BandWidthException.class)
    public void not_send_message_when_senddate_in_past() throws Exception {
        message.setSendDate(LocalDate.now().minusDays(1));
        int result = sender.send(message);
        assertThat(result, is(equalTo(-1)));
    }

    @Test(expected = BandWidthException.class)
    public void not_send_message_when_delete_is_not_processing() throws Exception {
        message.setDeleted(true);
        int result = sender.send(message);
        assertThat(result, is(equalTo(-1)));
    }

    @Test
    public void return200_when_successfully_send_message() throws Exception {

        int result = sender.send(message);
        assertThat(result >= 200, is(equalTo(true)));

    }
}
