package com.murray.communications.adapters.web.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murray.communications.adapters.web.SmsMessageAdapter;
import com.murray.communications.controllers.rest.UserAuthenticationRequest;
import com.murray.communications.domain.entities.messages.SmsMessage;
import com.murray.communications.domain.entities.users.ApplicationUser;
import com.murray.communications.domain.respository.messages.SmsMessageRepository;
import com.murray.communications.dtos.rest.SmsDto;
import com.murray.communications.dtos.web.NewSMSMessageDto;
import com.murray.communications.dtos.web.SMSMessageDto;
import com.murray.communications.dtos.web.SmsSearchResults;
import com.murray.communications.security.AuthenticationFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.murray.communications.security.jwt.enums.JwtTokenKey.REQUEST_HEADER_PREFIX;

/**
 * {@inheritDoc}
 */
@Slf4j
public class WebMessageAdapterImpl implements SmsMessageAdapter {

    private final RestTemplate restTemplate;


    private final AuthenticationFacade authenticationFacade;
    private final String baseUrl;
    private final ConversionService conversionService;
    private final SmsMessageRepository smsMessageRepository;

    public WebMessageAdapterImpl(AuthenticationFacade authenticationFacade, String baseUrl, ConversionService conversionService, SmsMessageRepository smsMessageRepository) {
        this.authenticationFacade = authenticationFacade;
        this.baseUrl = baseUrl;
        this.conversionService = conversionService;
        this.smsMessageRepository = smsMessageRepository;
        this.restTemplate = new RestTemplate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public SmsSearchResults findAllMessages(Integer page, Integer size) {

        Pageable sortedByMessageDescending = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<SmsMessage> pageResults = smsMessageRepository.findAllByDeletedIsFalse(sortedByMessageDescending);

        SmsSearchResults results = new SmsSearchResults();
        results.setMessages(messageDtos(pageResults));
        results.setPageNumber(pageResults.getNumber());
        results.setTotalElements(pageResults.getTotalElements());
        results.setTotalPages(pageResults.getTotalPages());


        return results;
    }

    private List<SMSMessageDto> messageDtos(Page<SmsMessage> page) {

        List<SMSMessageDto> messageDtos = new ArrayList<>();
        page.get().forEach(
                msg -> {
                    messageDtos.add(conversionService.convert(msg, SMSMessageDto.class));
                }
        );
        return messageDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTokenForLoggedUser() {

        ApplicationUser user = authenticationFacade.getAuthenticationUser();
        HttpEntity<UserAuthenticationRequest> userAuthenticationRequestHttpEntity = new HttpEntity<>(UserAuthenticationRequest.builder()
                .username(user.getUsername()).password(user.getPassword()).build(), httpHeaders());

        ResponseEntity<String> response = restTemplate
                .exchange(baseUrl.concat("/auth/signin"),
                        HttpMethod.POST, userAuthenticationRequestHttpEntity, String.class);

        return response.getBody();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SMSMessageDto saveSms(final NewSMSMessageDto newSMSMessageDto) {

        SmsDto smsDto = conversionService.convert(newSMSMessageDto, SmsDto.class);

        String token = getTokenForLoggedUser();

        ObjectMapper objectMapper =  new ObjectMapper();


        try {
            String test = objectMapper.writeValueAsString(smsDto);
            log.info("jsont to send:{}",test);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpHeaders httpHeaders = httpHeaders(token);
        HttpEntity<SmsDto> smsDtoHttpEntity = getSmsEntity(smsDto, httpHeaders);

        ResponseEntity<SmsDto> responseEntity = restTemplate.exchange(
                baseUrl.concat("/v1/sms"),
                HttpMethod.POST, smsDtoHttpEntity, SmsDto.class
        );

        return conversionService.convert(responseEntity.getBody(), SMSMessageDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SMSMessageDto findMessageBy(Long id) {

        String token = getTokenForLoggedUser();

        HttpHeaders httpHeaders = httpHeaders(token);
        HttpEntity<SmsDto> smsDtoHttpEntity = getSmsEntity(new SmsDto(), httpHeaders);

        ResponseEntity<SmsDto> responseEntity = restTemplate.exchange(
                baseUrl.concat("/v1/sms/" + id),
                HttpMethod.GET, smsDtoHttpEntity, SmsDto.class
        );

        return conversionService.convert(responseEntity.getBody(), SMSMessageDto.class);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SMSMessageDto update(SMSMessageDto messageDto) {
        String token = getTokenForLoggedUser();
        HttpHeaders httpHeaders = httpHeaders(token);
        SmsDto smsDto = conversionService.convert(messageDto, SmsDto.class);
        HttpEntity<SmsDto> smsDtoHttpEntity = getSmsEntity(smsDto, httpHeaders);

        ResponseEntity<SmsDto> responseEntity = restTemplate.exchange(
                baseUrl.concat("/v1/sms/" + messageDto.getId()),
                HttpMethod.PUT, smsDtoHttpEntity, SmsDto.class
        );

        return conversionService.convert(responseEntity.getBody(), SMSMessageDto.class);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {

        String token = getTokenForLoggedUser();
        HttpHeaders httpHeaders = httpHeaders(token);
        SmsDto smsDto = conversionService.convert(new SmsDto(), SmsDto.class);
        HttpEntity<SmsDto> smsDtoHttpEntity = getSmsEntity(smsDto, httpHeaders);

        ResponseEntity responseEntity = restTemplate.exchange(
                baseUrl.concat("/v1/sms/" + id),
                HttpMethod.DELETE, smsDtoHttpEntity, SmsDto.class
        );

        if(!responseEntity.getStatusCode().is2xxSuccessful()){
            throw new IllegalArgumentException("Issue deleting sms");
        }

    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(Long id) {

        String token = getTokenForLoggedUser();
        HttpHeaders httpHeaders = httpHeaders(token);
        SmsDto smsDto = conversionService.convert(new SmsDto(), SmsDto.class);
        HttpEntity<SmsDto> smsDtoHttpEntity = getSmsEntity(smsDto, httpHeaders);

        ResponseEntity responseEntity = restTemplate.exchange(
                baseUrl.concat("/v1/sms/").concat(Long.toString(id)).concat("/send"),
                HttpMethod.PUT, smsDtoHttpEntity, SmsDto.class
        );

        if(!responseEntity.getStatusCode().is2xxSuccessful()){
            throw new IllegalArgumentException("Issue deleting sms");
        }
    }

    HttpEntity<SmsDto> getSmsEntity(SmsDto dto, HttpHeaders httpHeaders) {

        return new HttpEntity<>(dto, httpHeaders);
    }

    HttpHeaders httpHeaders(final String token) {
        HttpHeaders httpHeaders = httpHeaders();
        httpHeaders.add("Authorization", REQUEST_HEADER_PREFIX.getName() + token);

        return httpHeaders;
    }

    HttpHeaders httpHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptableMediaTypes());
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        return headers;
    }

    private List<MediaType> acceptableMediaTypes() {

        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

        return acceptableMediaTypes;

    }
}
