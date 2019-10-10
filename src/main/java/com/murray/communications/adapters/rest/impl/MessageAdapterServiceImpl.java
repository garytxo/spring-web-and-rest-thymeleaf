package com.murray.communications.adapters.rest.impl;

import com.murray.communications.domain.entities.messages.SmsMessage;
import com.murray.communications.dtos.rest.SmsSearchDto;
import com.murray.communications.security.jwtaudit.AuditingCredentials;
import com.murray.communications.services.messages.MessageService;
import com.murray.communications.adapters.rest.MessageAdapterService;
import com.murray.communications.dtos.rest.SmsDto;
import com.murray.communications.exceptions.MessageCreationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * {@inheritDoc}
 */
@Slf4j
public class MessageAdapterServiceImpl implements MessageAdapterService {


    private final MessageService messageService;

    private final ConversionService conversionService;


    public MessageAdapterServiceImpl(MessageService messageService, ConversionService conversionService) {
        this.messageService = messageService;
        this.conversionService = conversionService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SmsDto find(Long id) {
        SmsMessage message = messageService.findBy(id);
        return conversionService.convert(message, SmsDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SmsDto save(AuditingCredentials credentials, SmsDto smsDto) {

        try {

            log.info("Saving smsDto");
            SmsMessage smsMessage = messageService.createSms(credentials, smsDto.getReceiver(), smsDto.getMessage(), smsDto.getSendOn());
            return conversionService.convert(smsMessage, SmsDto.class);

        } catch (NullPointerException np) {

            throw new MessageCreationException("Parameter null", np);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SmsDto update(AuditingCredentials credentials, SmsDto dto) {

        SmsMessage smsMessage = conversionService.convert(dto, SmsMessage.class);
        SmsMessage updated = messageService.update(credentials, smsMessage);
        return conversionService.convert(updated, SmsDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(AuditingCredentials credentials, Long smsId) {
        messageService.deleteMessageBy(credentials, smsId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SmsDto> findBy(SmsSearchDto smsSearch) {
        throw new NotImplementedException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendSms(AuditingCredentials credentials, Long id) {

        messageService.sendSms(credentials, id);

    }
}
