package com.spring.demo.config;

import com.spring.demo.exception.VeriticationCodeException;
import com.spring.demo.server.AuthUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationProvider extends DaoAuthenticationProvider {
    public MyAuthenticationProvider() {
    }

    public MyAuthenticationProvider(UserDetailsService userDetailsServiceBean, PasswordEncoder passwordEncoder) {
        this.setUserDetailsService(userDetailsServiceBean);
        this.setPasswordEncoder(passwordEncoder);
    }
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //super.additionalAuthenticationChecks(userDetails,authentication);
        MyAuthenticationDetails myAuthenticationDetails = (MyAuthenticationDetails) authentication.getDetails();
        if(!myAuthenticationDetails.isImageCodeIsRight()){
            throw new VeriticationCodeException();
        }
        super.additionalAuthenticationChecks(userDetails,authentication);
    }
}
