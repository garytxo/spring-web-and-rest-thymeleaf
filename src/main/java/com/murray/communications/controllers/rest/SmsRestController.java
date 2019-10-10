package com.murray.communications.controllers.rest;


import com.murray.communications.adapters.rest.MessageAdapterService;
import com.murray.communications.security.jwtaudit.AuditingCredentials;
import com.murray.communications.security.jwtaudit.Credentials;
import com.murray.communications.dtos.rest.SmsDto;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Spring MVC REST Controller for the SMS CRUD operations
 * Apart from that, it should only call the Adapter method and return the resulting objects.
 *
 * @see <a href="http://localhost:8080/swagger-ui.html">Swagger UI</a>
 * @see <a href="https://github.com/hantsy/springboot-jwt-sample">REST JWT </a>
 */
@Slf4j
@Api(value = "SMS CRUD Operations ",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
        ,authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)}
)
@RestController
@RequestMapping("/v1/sms")
public class SmsRestController {

    private final MessageAdapterService smsAdapterService;

    public SmsRestController(MessageAdapterService smsAdapterService) {
        this.smsAdapterService = smsAdapterService;
    }

    @PostMapping
    @ApiOperation(value = "Save a SMS message that has to be sent in the near future",
            authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Sms  successfully created", response = SmsDto.class),
            @ApiResponse(code = 406, message = "Error occurred creating SMS message")
    })
    @ResponseStatus(code = HttpStatus.CREATED)
    public SmsDto save(
            @ApiParam(value = "The SMS details to be saved")
            @RequestBody SmsDto smsDto,
            @Credentials AuditingCredentials auditingCredentials) {

        log.info("Saving SMS:{}", smsDto);


        return smsAdapterService.save(auditingCredentials, smsDto);
    }

    @GetMapping(path = {"/{id}"})
    @ApiOperation(value = "Find message by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message found", response = SmsDto.class),
            @ApiResponse(code = 404, message = "Message not found")
    })
    @ResponseStatus(code = HttpStatus.OK)
    public SmsDto findById(
            @ApiParam(value = "The SMS unique id")
            @PathVariable("id") Long messageId,
            @Credentials AuditingCredentials auditingCredentials) {

        log.info("Find SMS by id:{}", messageId);


        return smsAdapterService.find(messageId);
    }


    @PutMapping(path = {"/{id}"})
    @ApiOperation(value = "Update SMS message details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message found", response = SmsDto.class),
            @ApiResponse(code = 404, message = "Message not found")
    })
    @ResponseStatus(code = HttpStatus.OK)
    public SmsDto update(
            @ApiParam(value = "The SMS unique id")
            @PathVariable("id") Long messageId,
            @RequestBody SmsDto smsDto,
            @Credentials AuditingCredentials auditingCredentials
    ) {

        log.info("Updating SMS with id:{}", messageId);

        return smsAdapterService.update(auditingCredentials,smsDto);

    }

    @DeleteMapping(path = {"/{id}"})
    @ApiOperation(value = "Update SMS message details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message deleted "),
            @ApiResponse(code = 404, message = "Message not found")
    })
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity delete(
            @ApiParam(value = "The SMS unique id")
            @PathVariable("id") Long messageId,
            @Credentials AuditingCredentials auditingCredentials) {

        log.info("Deleting SMS with id:{}", messageId);

        smsAdapterService.delete(auditingCredentials,messageId);

        return ResponseEntity.ok().build();
    }


    @PutMapping(path = {"/{id}/send"})
    @ApiOperation(value = "Update SMS message details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message found", response = SmsDto.class),
            @ApiResponse(code = 404, message = "Message not found")
    })
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity send(
            @ApiParam(value = "The SMS unique id")
            @PathVariable("id") Long messageId,
            @Credentials AuditingCredentials auditingCredentials
    ) {

        log.info("Sending SMS with id:{}", messageId);

        smsAdapterService.sendSms(auditingCredentials,messageId);

        return ResponseEntity.ok().build();
    }


}
