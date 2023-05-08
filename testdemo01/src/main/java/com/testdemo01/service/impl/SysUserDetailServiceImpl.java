package com.testdemo01.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.testdemo01.entity.SysUser;
import com.testdemo01.security.AccountUser;
import com.testdemo01.service.SysUserService;

@Service
public class SysUserDetailServiceImpl implements UserDetailsService {


    @Autowired
    SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser sysuser = sysUserService.getByUsername(username);
        if(sysuser==null){
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        // 权限返回
        return new AccountUser(sysuser.getId(), sysuser.getUsername(), sysuser.getPassword(), getUserAuthority(sysuser.getId()));
    }

    /**
     * 获取用户权限
     * @param UserId
     * @return
     */
    public List<GrantedAuthority> getUserAuthority(Long UserId){

        String authority = sysUserService.getUserAuthorityInfo(UserId);


        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
    
}
