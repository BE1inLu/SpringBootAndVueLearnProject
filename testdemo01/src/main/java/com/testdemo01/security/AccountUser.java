package com.testdemo01.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

// 继承 user 类，在此基础上增加一个 userid 关键字
public class AccountUser extends User{

    @Getter
    @Setter
    private Long UserId;

    public AccountUser(Long UserId,String username,String password,Collection<? extends GrantedAuthority> authorities){
        this(UserId,username,password,true,true,true,true,authorities);
    }

    public AccountUser(Long UserId,String username, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.UserId=UserId;
    }


}
