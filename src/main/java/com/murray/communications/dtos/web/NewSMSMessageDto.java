package com.murray.communications.dtos.web;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class NewSMSMessageDto {

    @NotNull
    @Min(8)
    private Long receiver;

    private Long sender;

    @NotBlank(message = "Message cannot be blank")
    @Size(max = 500, message
            = "Message less that 500 character")
    private String message;

    @NotBlank(message = "Send date is mandatory")
    @Pattern(regexp = "^(((0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])/2[0-9])[0-9]{2})",
            message = "send date format in correct MM/DD/YYYY")
    private String sendOn;
}
