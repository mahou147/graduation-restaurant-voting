package com.github.mahou147.voting.to;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mahou147.voting.HasIdAndEmail;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserTo extends BaseTo implements HasIdAndEmail {

    @NotBlank
    @Size(min = 2, max = 128)
    String firstName;

    @NotBlank
    @Size(min = 2, max = 128)
    String lastName;

    @Email
    @NotBlank
    @Size(max = 128)
    String email;

    @NotBlank
    @Size(min = 5, max = 32)
    String password;

    @JsonCreator
    public UserTo(@JsonProperty("id") Integer id, @JsonProperty("email") String email,
                  @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName,
                  @JsonProperty("password") String password) {
        super(id);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserTo:" + id + '[' + email + ']';
    }
}
