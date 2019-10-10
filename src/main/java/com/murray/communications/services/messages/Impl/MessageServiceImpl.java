package com.murray.communications.services.messages.Impl;

import com.murray.communications.security.jwtaudit.AuditingCredentials;
import com.murray.communications.services.messages.MessageService;
import com.murray.communications.domain.entities.messages.SmsMessage;
import com.murray.communications.domain.entities.users.ApplicationUser;
import com.murray.communications.domain.respository.messages.SmsMessageRepository;
import com.murray.communications.domain.respository.users.ApplicationUserRepository;
import com.murray.communications.dtos.enums.MessageStatus;
import com.murray.communications.exceptions.BandWidthException;
import com.murray.communications.exceptions.MessageNotFoundException;
import com.murray.communications.exceptions.MessageProcessingException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * {@inheritDoc}
 */
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final SmsMessageRepository smsMessageRepository;

    private final ApplicationUserRepository userRepository;

    private final BandwidthMessageSender smsSender;

    public MessageServiceImpl(SmsMessageRepository smsMessageRepository, ApplicationUserRepository userRepository) {
        this.smsMessageRepository = smsMessageRepository;
        this.userRepository = userRepository;
        this.smsSender = new BandwidthMessageSender();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SmsMessage findBy(Long id) {
        return smsMessageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException("No messaged found for id:" + id));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public SmsMessage createSms(@NonNull AuditingCredentials auditingCredentials, @NonNull Long receiver, @NonNull String message, @NonNull LocalDate sendOn) {

        ApplicationUser user = userRepository.getOne(auditingCredentials.getUserId());

        log.info("saving sms sender:{} , receiver:{}, message:{}", user.getBandWidthCredentials().getSenderPhoneNumber(), receiver, message);
        SmsMessage smsMessage = new SmsMessage(receiver,user.getBandWidthCredentials().getSenderPhoneNumber(), message);
        smsMessage.setBandWidthCredentials(user.getBandWidthCredentials());
        smsMessage.setSendDate(sendOn);
        smsMessage.setCreatedDate(LocalDateTime.now());
        smsMessage.setCreatedBy(user);

        return smsMessageRepository.save(smsMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SmsMessage update(@NonNull AuditingCredentials auditingCredentials, SmsMessage smsMessage) {

        log.info("updating sms:{}", smsMessage);
        SmsMessage message = smsMessageRepository.findById(smsMessage.getId())
                .orElseThrow(() -> new MessageNotFoundException("No messaged found for id:" + smsMessage.getId()));

        message.setStatus(smsMessage.getStatus());
        message.setReceiver(smsMessage.getReceiver());
        message.setSender(smsMessage.getSender());
        message.setMessage(smsMessage.getMessage());
        message.setSendDate(smsMessage.getSendDate());

        ApplicationUser user = userRepository.getOne(auditingCredentials.getUserId());
        message.setLastModifiedDate(LocalDateTime.now());
        message.setLastModifiedBy(user);

        return smsMessageRepository.save(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMessageBy(@NonNull AuditingCredentials auditingCredentials, Long id) {

        log.info("deleting sms:{}", id);
        SmsMessage message = smsMessageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException("No messaged found for id:" + id));

        ApplicationUser user = userRepository.getOne(auditingCredentials.getUserId());

        message.setStatus(MessageStatus.DELETED);
        message.setDeleted(true);
        message.setLastModifiedDate(LocalDateTime.now());
        message.setLastModifiedBy(user);

        smsMessageRepository.save(message);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void sendSms(AuditingCredentials credentials, Long id) {

        log.info("attempting to send sms:{}", id);
        SmsMessage message = smsMessageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException("No messaged found for id:" + id));

        //send
        log.warn("SENDING SMS {} with app id details {}",message.getId(),message.getBandWidthCredentials().getApplicationId());

        MessageStatus newStatus = sendMessage(message);

        ApplicationUser user = userRepository.getOne(credentials.getUserId());
        message.setStatus(newStatus);
        message.setLastModifiedDate(LocalDateTime.now());
        message.setLastModifiedBy(user);

        smsMessageRepository.save(message);

        if(newStatus.equals(MessageStatus.ERROR)){
            throw  new MessageProcessingException("Issue processing message with bandwidth with id:"+message.getId());
        }

    }

    /**
     *
     * @param message
     * @return
     */
    private MessageStatus  sendMessage(SmsMessage message){

        MessageStatus newStatus = MessageStatus.PROCESSING;

        try {
            int result = smsSender.send(message);
            log.info("Message id:{} sent successfully to bandwith for processing response code:{}",message.getId(),result);

        } catch (BandWidthException e) {

            log.error("Error sending message to bandwith with id:{}",message.getId(),e);

            newStatus = MessageStatus.ERROR;
        }

        return newStatus;
    }
}
