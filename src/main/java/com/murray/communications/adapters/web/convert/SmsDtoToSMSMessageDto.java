package com.murray.communications.adapters.web.convert;

import com.murray.communications.services.formatters.CustomDateFormatter;
import com.murray.communications.dtos.rest.SmsDto;
import com.murray.communications.dtos.web.SMSMessageDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SmsDtoToSMSMessageDto implements Converter<SmsDto, SMSMessageDto> {

    private final CustomDateFormatter formatter;

    public SmsDtoToSMSMessageDto(CustomDateFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public SMSMessageDto convert(SmsDto restDto) {

        SMSMessageDto dto = new SMSMessageDto();
        dto.setId(restDto.getId());
        dto.setReceiver(restDto.getReceiver());
        dto.setSender(restDto.getSender());
        dto.setMessage(restDto.getMessage());

        dto.setSendOn(restDto.getSendOn().format(formatter.localDateFormatter()));
        dto.setStatus(restDto.getStatus().name());
        dto.setApplicationName(restDto.getApplicationName());

        return dto;
    }
}
