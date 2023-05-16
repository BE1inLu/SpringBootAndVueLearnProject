package com.testdemo01.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


// 继承 user 类，在此基础上增加一个 userid 关键字
public class AccountUser implements UserDetails {

    private Long UserId;

    private String password;

    private final String username;

    private final Collection<? extends GrantedAuthority> authorities;

    private final boolean accountNonExpired;

    private final boolean accountNonLocked;

    private final boolean credentialsNonExpired;

    private final boolean enabled;

    public AccountUser(Long UserId, String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this(UserId, username, password, true, true, true, true, authorities);
    }

    public AccountUser(Long UserId, String username, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities) {
        this.UserId = UserId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        this.UserId = userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
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
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
