package com.murray.communications.adapters.rest.convert;

import com.murray.communications.domain.entities.messages.SmsMessage;
import com.murray.communications.dtos.rest.SmsDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SmsDTOtoSmsMessage implements Converter<SmsDto, SmsMessage> {

    @Override
    public SmsMessage convert(SmsDto dto) {
        SmsMessage smsMessage = new SmsMessage();

        smsMessage.setId(dto.getId());
        smsMessage.setStatus(dto.getStatus());
        smsMessage.setSendDate(dto.getSendOn());
        smsMessage.setReceiver(dto.getReceiver());
        smsMessage.setSender(dto.getSender());
        smsMessage.setMessage(dto.getMessage());


        return smsMessage;

    }
}
