package org.akhil.authorizationserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.akhil.authorizationserver.enums.AuthenticationType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "app_user_role", joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private AuthenticationType authenticationType;

    private boolean expired = false;

    private boolean locked = false;

    private boolean credentialsExpired = false;

    private boolean disabled = false;

    @OneToOne(mappedBy = "user")
    private ForgotPassword forgotPassword;


    /**
     * Returns the authorities granted to the user. Cannot return null.
     *
     * @return the authorities, sorted by natural key (never null)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     */
    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     */
    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     */
    @Override
    public boolean isEnabled() {
        return !disabled;
    }

    public static String getEmailUsername(String email) {
        // Split the email address by '@' symbol
        String[] parts = email.split("@");

        // The username is the first part before the '@' symbol
        return parts[0];
    }
    public static AppUser fromOauth2User(OAuth2User user) {
        AppUser googleUser = AppUser.builder()
                .username(getEmailUsername(user.getName()))
                .email(user.getName())
                .name(user.getAttributes().get("name").toString())
                .authenticationType(AuthenticationType.valueOf("GOOGLE"))
                .build();
        return googleUser;
    }
}
