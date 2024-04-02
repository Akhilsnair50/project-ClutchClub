package org.akhil.authorizationserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

    private boolean expired = false;

    private boolean locked = false;

    private boolean credentialsExpired = false;

    private boolean disabled = false;


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
}
