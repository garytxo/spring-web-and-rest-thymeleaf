package com.murray.communications.dtos.web;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SMSMessageDto extends NewSMSMessageDto {

    private Long id;

    private LocalDateTime createdOn;

    private String status;

    private String applicationName;

}
