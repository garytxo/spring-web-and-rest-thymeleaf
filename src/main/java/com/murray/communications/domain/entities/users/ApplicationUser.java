package com.murray.communications.domain.entities.users;

import com.murray.communications.domain.entities.messages.BandWidthCredentials;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * The AuthenticatedUser is a standard JPA entity which implements the
 * Spring Security specific UserDetails interface.
 */
@Entity
@Table(name = "application_user", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    Long id;

    @Column(name = "username")
    @Email(message = "username should be a valid email address")
    @NotEmpty(message = "username field is mandatory")
    private String username;


    @Length(min = 8, message = "Password must have at least 8 characters")
    @NotEmpty(message = "Password is mandatory")
    private String password;

    private Boolean enabled;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "application_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<ApplicationRole> roles;

    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_bandwidth_credentials",
            joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "bandwidth_id", referencedColumnName = "id") })
    private BandWidthCredentials bandWidthCredentials;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(ApplicationRole::getRole)
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }

    public List<String> gerRolesOnly() {
        return
                this.roles.stream()
                        .map(ApplicationRole::getRole)
                        .collect(toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


}
