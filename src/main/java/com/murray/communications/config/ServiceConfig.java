package com.murray.communications.config;

import com.murray.communications.adapters.web.SmsMessageAdapter;
import com.murray.communications.adapters.web.impl.WebMessageAdapterImpl;
import com.murray.communications.services.messages.Impl.MessageServiceImpl;
import com.murray.communications.services.messages.MessageService;
import com.murray.communications.adapters.rest.MessageAdapterService;
import com.murray.communications.adapters.rest.impl.MessageAdapterServiceImpl;
import com.murray.communications.domain.respository.messages.SmsMessageRepository;
import com.murray.communications.domain.respository.users.ApplicationUserRepository;
import com.murray.communications.security.AuthenticationFacade;
import com.murray.communications.security.jwt.JwtTokenProviderService;
import com.murray.communications.services.formatters.CustomDateFormatter;
import com.murray.communications.services.formatters.impl.CustomDateFormatterImpl;
import com.murray.communications.services.users.UserAuthenticatonService;
import com.murray.communications.services.users.impl.UserAuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class ServiceConfig {

    @Bean
    public SmsMessageAdapter webMessageAdapterService(AuthenticationFacade authenticationFacade,
                                                      @Value("${communication.base-url}")
                                                              String baseUrl,
                                                      ConversionService conversionService,
                                                      SmsMessageRepository smsMessageRepository) {

        return new WebMessageAdapterImpl(authenticationFacade, baseUrl, conversionService,smsMessageRepository);
    }

    @Bean
    public MessageAdapterService smsAdapterService(MessageService messageService, ConversionService conversionService) {

        return new MessageAdapterServiceImpl(messageService, conversionService);
    }

    @Bean
    public MessageService messageService(SmsMessageRepository smsMessageRepository,ApplicationUserRepository userRepository) {

        return new MessageServiceImpl(smsMessageRepository,userRepository);
    }

    @Bean
    public UserAuthenticatonService userAuthenticatonService(AuthenticationManager authenticationManager,
                                                             ApplicationUserRepository userRepository, JwtTokenProviderService jwtTokenProviderService) {

        return new UserAuthenticationServiceImpl(authenticationManager, userRepository, jwtTokenProviderService);
    }

    @Bean
    public JwtTokenProviderService jwtTokenProviderService(@Value("${security.jwt.token.secret-key:secret}")
                                                                   String secretKey,
                                                           @Value("${security.jwt.token.expire-length:3600000}")
                                                                   long validityInMilliseconds,

                                                           UserDetailsService userDetailsService) {

        return new JwtTokenProviderService(secretKey, validityInMilliseconds, userDetailsService);
    }

    @Bean
    public CustomDateFormatter customDateFormatter() {

        return new CustomDateFormatterImpl();
    }
}
