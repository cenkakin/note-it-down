package com.github.noteitdown.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.noteitdown.auth.request.SignUpRequest;
import com.github.noteitdown.common.security.ExtendedUserDetails;
import com.github.noteitdown.common.security.UserRole;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Created by cenkakin
 */
@Document
public class User implements ExtendedUserDetails {

    @Id
    private String id;

    @NotBlank
    @Size(max = 100)
    @Indexed(unique = true)
    private String email;

    @NotBlank
    @Size(max = 100)
    @JsonIgnore
    private String password;

    private Boolean active;

    private Set<UserRole> roles;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastModifiedAt;

    private User(String email, String password, Boolean active, Set<UserRole> roles) {
        this.email = email;
        this.password = password;
        this.active = active;
        this.roles = roles;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Instant lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", roles=" + roles.toString() +
                ", createdAt=" + createdAt +
                ", lastModifiedAt=" + lastModifiedAt +
                '}';
    }

    public static User fromSignUpRequest(SignUpRequest request) {
        return new User(request.getEmail(), request.getPassword(), true, Set.of(UserRole.USER));
    }
}
