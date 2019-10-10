package com.murray.communications.domain.respository.messages;

import com.murray.communications.domain.entities.messages.SmsMessage;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("classpath:test-data.sql")
public class SmsMessageRepositoryTest {

    @Autowired
    private SmsMessageRepository smsMessageRepository;

    @Test
    public void saveSms_returnAuditFields() {

        SmsMessage message = new SmsMessage(12121L, 44444L, "Test");
        message.setCreatedDate(LocalDateTime.now());
        smsMessageRepository.save(message);

        SmsMessage result = smsMessageRepository.findById(message.getId()).orElseThrow(() -> new NullPointerException());

        assertThat(result, is(notNullValue()));

        MatcherAssert.assertThat(result.getId(), is(notNullValue()));

        MatcherAssert.assertThat(result.getCreatedDate(), is(notNullValue()));


    }

    @Test
    public void findAll_returnsThreeOrMore() {

        Iterable<SmsMessage> messages = smsMessageRepository.findAll();

        assertThat(messages, is(notNullValue()));

        assertThat(messages.spliterator().getExactSizeIfKnown() >= 3L, is((true)));


    }

    @Test
    public void findFirstThree_orderMessageDescending() {

        Pageable sortedByMessageDescending = PageRequest.of(0, 3, Sort.by("message").descending());
        Page<SmsMessage> results = smsMessageRepository.findAll(sortedByMessageDescending);

        assertThat(results, is(notNullValue()));
        assertThat(results.getTotalElements() > 10L, is(equalTo(true)));
        assertThat(results.getTotalPages() > 3L, is(equalTo(true)));
        assertThat(results.get().count(), is(equalTo(3L)));


    }

    @Test
    public void findSecondThree_orderMessageDescending() {

        Pageable sortedByMessageDescending = PageRequest.of(1, 3, Sort.by("message").descending());
        Page<SmsMessage> results = smsMessageRepository.findAll(sortedByMessageDescending);

        assertThat(results, is(notNullValue()));
        assertThat(results.getTotalElements() > 10L, is(equalTo(true)));
        assertThat(results.getTotalPages() > 3L, is(equalTo(true)));
        assertThat(results.get().count(), is(equalTo(3L)));
        System.out.println("tot elements:" + results.getTotalElements());
        System.out.println("tot pages:" + results.getTotalPages());
        System.out.println("tot number:" + results.getNumber());
        System.out.println("tot NumberOfElements:" + results.getNumberOfElements());

        System.out.println("tot getPageable:" + results.getPageable());
        System.out.println("tot nextPageable:" + results.nextPageable());


    }

    @Test
    public void findSenderMessageSentTwoWeeksAgo_returnThree() {

        long returnThree = 3L;
        long sender = 33333333L;
        LocalDate from = LocalDate.now().minusDays(14);
        LocalDate to = LocalDate.now().minusDays(7);

        Pageable sortedByMessageDescending = PageRequest.of(0, 10, Sort.by("SendDate"));

        Page<SmsMessage> results = smsMessageRepository.findAllBySendDateBetweenAndSenderAndDeletedIsFalse(from, to, sender, sortedByMessageDescending);

        assertThat(results, is(notNullValue()));

        results.get().forEach(
                msg -> System.out.println(msg.getId() + " sentOn:" + msg.getSendDate() + ", message:" + msg.getMessage())
        );

        assertThat(results.getTotalElements(), is(equalTo(3L)));


    }

}
