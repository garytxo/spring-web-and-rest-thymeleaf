package com.murray.communications.adapters.web.convert;

import com.murray.communications.domain.entities.messages.SmsMessage;
import com.murray.communications.dtos.web.SMSMessageDto;
import com.murray.communications.services.formatters.CustomDateFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SmsMessageToSMSMessageDTo  implements Converter<SmsMessage, SMSMessageDto> {

    private final CustomDateFormatter formatter;

    public SmsMessageToSMSMessageDTo(CustomDateFormatter formatter) {

        this.formatter = formatter;
    }

    @Override
    public SMSMessageDto convert(SmsMessage smsMessage) {

        SMSMessageDto dto = new SMSMessageDto();
        dto.setStatus(smsMessage.getStatus().name());
        dto.setId(smsMessage.getId());
        dto.setSendOn(smsMessage.getSendDate().format(formatter.localDateFormatter()));
        dto.setMessage(smsMessage.getMessage());
        dto.setSender(smsMessage.getSender());
        dto.setReceiver(smsMessage.getReceiver());
        dto.setCreatedOn(smsMessage.getCreatedDate());
        dto.setApplicationName(smsMessage.getBandWidthCredentials().getApplicationName());

        return dto;
    }
}
