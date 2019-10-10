package com.murray.communications.adapters.web.convert;

import com.murray.communications.dtos.enums.MessageStatus;
import com.murray.communications.dtos.rest.SmsDto;
import com.murray.communications.dtos.web.SMSMessageDto;
import com.murray.communications.services.formatters.CustomDateFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SMSMessageDtoToRestSmsDto implements Converter<SMSMessageDto, SmsDto> {

    private final CustomDateFormatter formatter;

    public SMSMessageDtoToRestSmsDto(CustomDateFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public SmsDto convert(SMSMessageDto messageDto) {

        SmsDto dto = new SmsDto();
        dto.setStatus(MessageStatus.valueOf(messageDto.getStatus()));
        dto.setSendOn(LocalDate.parse(messageDto.getSendOn(), formatter.localDateFormatter()));
        dto.setMessage(messageDto.getMessage());
        dto.setSender(messageDto.getSender());
        dto.setReceiver(messageDto.getReceiver());
        dto.setId(messageDto.getId());
        dto.setApplicationName(messageDto.getApplicationName());

        return dto;
    }
}
