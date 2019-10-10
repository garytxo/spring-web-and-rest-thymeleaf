package com.murray.communications.domain.entities.messages;


import com.murray.communications.domain.entities.AbstractAuditableEntity;
import com.murray.communications.domain.entities.users.ApplicationUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * Holds user specific bandwidth details
 */
@Entity
@Table(name = "bandwidth_credentials")
@Getter
@Setter
public class BandWidthCredentials extends AbstractAuditableEntity<ApplicationUser, Long> implements Serializable {


    @OneToOne(mappedBy = "bandWidthCredentials")
    private ApplicationUser user;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy="bandWidthCredentials")
    private List<SmsMessage> messages;

    /**
     * Bandwidth message API
     */
    @NotEmpty
    private String messageApi;

    /**
     *  Bandwidth message API secret
     */
    @NotEmpty
    private String messageSecret;


    /**
     * User define name for the bandwidth details
     */
    @NotEmpty
    private String applicationName;

    /**
     * Bandwidth application id
     */
    @NotEmpty
    private String applicationId;

    /**
     * Actual valid phone number defined and accociated with the
     * bandwidth sub account location
     */
    @NotEmpty
    private Long senderPhoneNumber;


    /**
     * Dash board user name
     */
    @NotEmpty
    private String dashboardUserName;

    /**
     * Dash board user password
     */
    @NotEmpty
    private String dashboardPwd;

    /**
     * Dash board  valid account id account
     */
    @NotEmpty
    private String dashboardAccountId;

    /**
     * Dashboard subaccount
     */
    @NotEmpty
    private String dashboardSubAccountId;







}
