package com.spring.demo.server;

import com.spring.demo.mapper.UserMapper;
import com.spring.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthUserDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(!StringUtils.isEmpty(username)){
            User user = userMapper.getUserByName(username);
            if(user == null){
                throw new UsernameNotFoundException("用户名不存在");
            }
            user.setAuthoritys(AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
            return user;
        }
        return null;
    }

    /**
     * 自定义权限解析
     * @param roles
     * @return
     */
    private List<GrantedAuthority> grantedAuthorities(String roles){
        List<GrantedAuthority> list = new ArrayList<>();
        if(!StringUtils.isEmpty(roles)){
            if(roles.contains(",")) {
                String[] array = roles.split(",");
                for (String role:array) {
                    list.add(new SimpleGrantedAuthority(role));
                }
            }else{
                list.add(new SimpleGrantedAuthority(roles));
            }
        }
        return list;
    }
}
