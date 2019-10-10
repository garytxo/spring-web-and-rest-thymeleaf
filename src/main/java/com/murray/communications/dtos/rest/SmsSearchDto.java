package com.murray.communications.dtos.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.murray.communications.dtos.enums.MessageStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Transfer search criteria
 */
@ApiModel(value = "SmsSearchDto",
        description = "defines  search criteria that can be use to" +
                "search for sms messages."
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsSearchDto {

    @JsonProperty(value = "id")
    @ApiModelProperty(value = "Sms unique internal id")
    private Long id;

    @JsonProperty(value = "createdBetween")
    @ApiModelProperty(value = "Filter between creation dates, usually define  two dates",
            example = "[{2019-09-27,2019-09-31}]")
    private List<LocalDate> createdBetween;


    @JsonProperty(value = "sendOnBetween")
    @ApiModelProperty(value = "Filter between dates when message to be sent, " +
            "usually define  two dates",
            example = "[{2019-09-27,2019-09-31}]")
    private List<LocalDate> sendOnBetween;

    @JsonProperty(value = "sender")
    @ApiModelProperty(value = "filter on sender number")
    private Integer sender;

    @JsonProperty(value = "receiver")
    @ApiModelProperty(value = "filter on receiver number ")
    private Integer receiver;

    @JsonProperty(value = "status")
    @ApiModelProperty(value = "filter on message number ",
            allowableValues = "CREATED, SEND, ERROR")
    private MessageStatus status;
}
