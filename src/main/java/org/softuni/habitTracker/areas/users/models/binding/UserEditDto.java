package org.softuni.habitTracker.areas.users.models.binding;

import org.softuni.habitTracker.areas.users.util.Constants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

public class UserEditDto {
    private Long id;

    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9.-_]*[A-Za-z0-9]@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\\w+$", message = Constants.INVALID_EMAIL)
    @NotNull
    private String email;

    private String firstName;

    private String lastName;

    private Set<String> authorities;

    public UserEditDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

}
