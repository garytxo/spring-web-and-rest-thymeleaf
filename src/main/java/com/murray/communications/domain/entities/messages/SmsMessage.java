package com.murray.communications.domain.entities.messages;

import com.murray.communications.domain.entities.AbstractAuditableEntity;
import com.murray.communications.domain.entities.users.ApplicationUser;
import com.murray.communications.dtos.enums.MessageStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "sms_message")
@Data
public class SmsMessage extends AbstractAuditableEntity<ApplicationUser, Long> implements Serializable {

    /**
     * Message status, indicating if the message has been delivered, error etc...
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "bandwidth_id")
    private BandWidthCredentials bandWidthCredentials;
    /**
     * Phone number that received the message
     */
    private Long receiver;

    /**
     * Phone number used for sending the message
     */
    private Long sender;

    /**
     * Actual sms content
     */
    private String message;

    /**
     * Timestamp when the message was send to bandwidth for processing
     */
    private LocalDate sendDate;

    /**
     * Bandwidth delivery response which is return on the callback hook.
     */
    @Column
    private String deliverResponse;


    @Column
    boolean deleted  = false;

    public SmsMessage() {
    }

    public SmsMessage(final @NonNull Long receiver, final @NonNull Long sender, final @NonNull String message) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
        this.status = MessageStatus.CREATED;
    }

}
