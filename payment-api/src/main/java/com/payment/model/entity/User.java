package com.payment.model.entity;

import com.payment.model.Role;
import com.payment.model.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Data
@Inheritance
@DiscriminatorColumn(name = "user_type")
@DiscriminatorValue("user")
@Table(name = "__user",
indexes = @Index(name = "user_email_idx", columnList = "email"))
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    protected String name;
    protected String description;
    protected String email;

    @Getter(AccessLevel.NONE)
    protected String password;

    @Enumerated(EnumType.STRING)
    protected Status status;

    @Enumerated(EnumType.STRING)
    protected Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(
                String.format("ROLE_%s", role.getRoleName())
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

    @Override
    public boolean isEnabled() {
        return status == Status.ACTIVE;
    }
}
