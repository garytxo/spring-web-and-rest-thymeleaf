package com.murray.communications.adapters.web.convert;

import com.murray.communications.services.formatters.CustomDateFormatter;
import com.murray.communications.dtos.rest.SmsDto;
import com.murray.communications.dtos.web.NewSMSMessageDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class NewSMSMessageDtoToRestSmsDto implements Converter<NewSMSMessageDto, SmsDto> {

    private final CustomDateFormatter formatter;

    public NewSMSMessageDtoToRestSmsDto(CustomDateFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public SmsDto convert(NewSMSMessageDto newSMSMessage) {

        SmsDto dto = new SmsDto();
        dto.setReceiver(newSMSMessage.getReceiver());
        dto.setSender(newSMSMessage.getSender());
        dto.setMessage(newSMSMessage.getMessage());
        dto.setSendOn(LocalDate.parse(newSMSMessage.getSendOn(), formatter.localDateFormatter()));

        return dto;
    }
}
