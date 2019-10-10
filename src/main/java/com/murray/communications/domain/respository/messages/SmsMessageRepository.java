package com.murray.communications.domain.respository.messages;

import com.murray.communications.domain.entities.messages.SmsMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SmsMessageRepository extends PagingAndSortingRepository<SmsMessage, Long> {


    /**
     * find all sms send between the two date and sent by a specific number findAllBySendOnBetweenAndSender
     */
    Page<SmsMessage> findAllBySendDateBetweenAndSenderAndDeletedIsFalse(LocalDate from, LocalDate to, Long sender, Pageable pageable);

    /**
     * Find all that are not deleted
     * @param pageable
     * @return
     */
    Page<SmsMessage> findAllByDeletedIsFalse(Pageable pageable);


}
