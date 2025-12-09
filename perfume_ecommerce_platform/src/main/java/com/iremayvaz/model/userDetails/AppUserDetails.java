package com.iremayvaz.model.userDetails;

import com.iremayvaz.model.entity.Role;
import com.iremayvaz.model.entity.User;
import com.iremayvaz.model.enums.RoleName;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class AppUserDetails implements UserDetails {

    private final User user;

    public AppUserDetails(User user) {
        this.user = user;
    }

    public static AppUserDetails from(User user) {
        return new AppUserDetails(user);
    }

    public Long getId() {
        return user.getId();
    }

    public Set<RoleName> getRoles() {
        return user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE_ADMIN, ROLE_USER şeklinde authority üret
        return user.getRoles();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();  // login email ile
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
