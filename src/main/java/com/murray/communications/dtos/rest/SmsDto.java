package com.murray.communications.dtos.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.murray.communications.dtos.enums.MessageStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Transfer transfer data object
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "SmsDto",
        description = "Represents a SMS message that is stored and sent either directly or in the future"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsDto {

    @JsonProperty(value = "id")
    @ApiModelProperty(value = "Sms unique internal id")
    private Long id;

    @JsonProperty(value = "createdOn")
    @ApiModelProperty(value = "Timestamp when the sms was saved")
    private LocalDateTime createdOn;

    @JsonProperty(value = "status")
    @ApiModelProperty(value = "SMS status ",
            allowableValues = "CREATED, SEND, ERROR",
            example = "CREATED"
    )
    private MessageStatus status;

    @JsonProperty(value = "receiver")
    @ApiModelProperty(value = "phone number that is to receive the message ",
            required = true,
            example = "4175409749"
    )
    private Long receiver;

    @JsonProperty(value = "sender")
    @ApiModelProperty(value = "phone number that is to sending the message ",
            example = "18445014846"
    )
    private Long sender;

    @JsonProperty(value = "message")
    @ApiModelProperty(value = "sms message content",
            example = "Hello world", required = true
    )
    private String message;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty(value = "sendOn")
    @ApiModelProperty(value = "Timestamp when the sms should be sent to the receiver",
            example = "YYYY-MM-dd : 2019-10-13")
    private LocalDate sendOn;


    @JsonProperty(value = "applicationName")
    @ApiModelProperty(value = "external application name that is processsing the message",
            example = "test-bandwidth-app"
    )
    private String applicationName;

}
