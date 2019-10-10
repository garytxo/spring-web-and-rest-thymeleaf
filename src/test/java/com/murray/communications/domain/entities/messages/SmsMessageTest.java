package com.murray.communications.domain.entities.messages;

import com.murray.communications.dtos.enums.MessageStatus;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class SmsMessageTest {

    @Test
    public void newInstance_status_is_created() {

        SmsMessage result = new SmsMessage(12312321L, 1212122L, "Test Message");

        assertThat(result, is(notNullValue()));
        assertThat(result.getStatus(), is(equalTo(MessageStatus.CREATED)));

    }

    @Test(expected = NullPointerException.class)
    public void newInstance_nullReceiver_throwError() {
        SmsMessage result = new SmsMessage(null, 1212122L, "Test Message");
    }

    @Test(expected = NullPointerException.class)
    public void newInstance_nullSender_throwError() {

        SmsMessage result = new SmsMessage(12312321L, null, "Test Message");
    }


    @Test(expected = NullPointerException.class)
    public void newInstance_nullMessage_throwError() {

        SmsMessage result = new SmsMessage(12312321L, 1212122L, null);
    }
}
