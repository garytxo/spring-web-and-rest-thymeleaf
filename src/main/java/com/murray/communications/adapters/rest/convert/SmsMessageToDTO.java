package com.murray.communications.adapters.rest.convert;

import com.murray.communications.domain.entities.messages.SmsMessage;
import com.murray.communications.dtos.rest.SmsDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SmsMessageToDTO implements Converter<SmsMessage, SmsDto> {

    @Override
    public SmsDto convert(SmsMessage smsMessage) {

        SmsDto dto = new SmsDto();

        dto.setMessage(smsMessage.getMessage());
        dto.setStatus(smsMessage.getStatus());
        dto.setId(smsMessage.getId());
        dto.setCreatedOn(smsMessage.getCreatedDate());
        dto.setReceiver(smsMessage.getReceiver());
        dto.setSender(smsMessage.getSender());
        dto.setSendOn(smsMessage.getSendDate());
        dto.setApplicationName(smsMessage.getBandWidthCredentials().getApplicationName());
        return dto;
    }
}
