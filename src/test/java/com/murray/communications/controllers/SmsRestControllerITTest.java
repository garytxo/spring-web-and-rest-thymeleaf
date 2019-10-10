package com.murray.communications.controllers;

import com.murray.communications.controllers.rest.UserAuthenticationRequest;
import com.murray.communications.dtos.enums.MessageStatus;
import com.murray.communications.dtos.rest.SmsDto;
import com.murray.communications.security.jwt.enums.JwtTokenKey;
import com.murray.communications.config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(TestConfig.class)
@Sql("classpath:test-data.sql")
public class SmsRestControllerITTest {



    @LocalServerPort
    private int port;

    private String token;

    @Before
    public void setup()  {
        RestAssured.port = this.port;
        token = given()
                .contentType(ContentType.JSON)
                .body(UserAuthenticationRequest.builder().username("gary@email.com").password("gary").build())
                .when().post("/auth/signin")
                .andReturn().asString();
    }

    @Test
     public void delete_return_200(){

        //@formatter:off
       given()
            .header(JwtTokenKey.AUTHORIZATION.getName(), JwtTokenKey.REQUEST_HEADER_PREFIX.getName()+token)
            .contentType(ContentType.JSON)

        .when()
            .delete("/v1/sms/3")

        .then()
            .statusCode(200);
        //@formatter:on
    }

    @Test
    public void put_send_message_invalid_return400(){
        //@formatter:off
        given()
                .header("Authorization", "Bearer "+token)
                .contentType(ContentType.JSON)
                .when()
                .put("/v1/sms/3/send")
                .then()
                .statusCode(400);
        //@formatter:on
    }

    @Test
    public void put_send_message_valid_return200(){
        //@formatter:off
        given()
                .header("Authorization", "Bearer "+token)
                .contentType(ContentType.JSON)
                .when()
                .put("/v1/sms/1/send")
                .then()
                .statusCode(200);
        //@formatter:on
    }

    @Test
    public void put_update_return_200(){

        LocalDate eightDays = LocalDate.now().plusDays(8);
        SmsDto update = new SmsDto();
        update.setStatus(MessageStatus.CREATED);
        update.setId(1L);
        update.setReceiver(999999L);
        update.setSender(8888888L);
        update.setMessage("Replace message");
        update.setSendOn(eightDays);

        //@formatter:off
        SmsDto result = given()
                .header("Authorization", "Bearer "+token)
                .contentType(ContentType.JSON)
                .body(update)

                .when()
                .put("/v1/sms/1")

                .then()
                .statusCode(200)
                .extract().as(SmsDto.class);
        //@formatter:on

        assertThat(result.getId(), is(equalTo(update.getId())));
        assertThat(result.getMessage(), is(equalTo(update.getMessage())));
        assertThat(result.getReceiver(), is(equalTo(update.getReceiver())));
        assertThat(result.getSender(), is(equalTo(update.getSender())));
        assertThat(result.getSendOn(), is(equalTo(update.getSendOn())));
        assertThat(result.getStatus(),is(equalTo(MessageStatus.CREATED)));

    }

    @Test
    public void get_message_returns_200(){

        //@formatter:off
        SmsDto result = given()
            .header("Authorization", "Bearer "+token)
            .contentType(ContentType.JSON)
        .when()
            .get("/v1/sms/1")

        .then()
            .statusCode(200)
            .extract().as(SmsDto.class);
        //@formatter:on
    }

    @Test
    public void get_not_exist_message_returns_404(){

        //@formatter:off
       given()
            .header("Authorization", "Bearer "+token)
            .contentType(ContentType.JSON)

        .when()
            .get("/v1/sms/190239")

        .then()
            .statusCode(404);
        //@formatter:on
    }

    @Test
    public void post_sms_returns_201() {

        SmsDto dto = new SmsDto();
        dto.setMessage("test message");
        dto.setReceiver(222222L);
        dto.setSender(22223232L );
        dto.setSendOn(LocalDate.now().plusDays(11L));

        //@formatter:off
        SmsDto result = given()
            .header("Authorization", "Bearer "+token)
            .contentType(ContentType.JSON)
            .body(dto)

        .when()
            .post("/v1/sms")

        .then()
            .statusCode(201)
            .extract().as(SmsDto.class);
        //@formatter:on

        assertThat(result.getId(), is(notNullValue()));
        assertThat(result.getStatus(),is(equalTo(MessageStatus.CREATED)));
    }

    @Test
    public void post_sms_returns_406() {

        SmsDto dto = new SmsDto();

        //@formatter:off
         given()
            .header("Authorization", "Bearer "+token)
            .contentType(ContentType.JSON)
            .body(dto)

        .when()
            .post("/v1/sms")

        .then()
            .statusCode(406);
        //@formatter:on


    }



}
