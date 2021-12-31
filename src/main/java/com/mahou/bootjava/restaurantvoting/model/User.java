package com.mahou.bootjava.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mahou.bootjava.restaurantvoting.HasIdAndEmail;
import com.mahou.bootjava.restaurantvoting.util.validation.NoHtml;
import lombok.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "user_unique_email_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity implements HasIdAndEmail, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NoHtml   // https://stackoverflow.com/questions/17480809
    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 128)
    private String email;

    @NoHtml
    @Column(name = "first_name", nullable = false)
    @NotBlank
    @Size(min = 2, max = 128)
    private String firstName;

    @NoHtml
    @NotBlank
    @Column(name = "last_name", nullable = false)
    @Size(min = 2, max = 128)
    private String lastName;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(max = 256)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    private boolean enabled = true;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date registered = new Date();

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), uniqueConstraints = {
            @UniqueConstraint(columnNames = {"user_id", "role"}, name = "user_roles_unique")})
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Role> roles;


    public User(User u) {
        this(u.id, u.email, u.firstName, u.lastName, u.password, u.enabled, u.registered, u.roles);
    }

    public User(Integer id, String email, String firstName, String lastName, String password, Role role, Role... roles) {
        this(id, email, firstName, lastName, password, true, new Date(), EnumSet.of(role, roles));
    }

    public User(Integer id, String email, String firstName, String lastName, String password, boolean enabled,
                Date registered, Collection<Role> roles) {
        super(id);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.enabled = enabled;
        this.registered = registered;
        setRoles(roles);
    }

    public void setEmail(String email) {
        this.email = StringUtils.hasText(email) ? email.toLowerCase() : null;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User:" + id + '[' + email + ']';
    }
}