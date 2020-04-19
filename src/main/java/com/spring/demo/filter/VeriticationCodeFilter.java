package com.spring.demo.filter;

import com.spring.demo.config.FailHandle;
import com.spring.demo.exception.VeriticationCodeException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class VeriticationCodeFilter extends OncePerRequestFilter {
    private AuthenticationFailureHandler failureHandler = new FailHandle();
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(!"/auth/form".equals(httpServletRequest.getRequestURI())){
             filterChain.doFilter(httpServletRequest,httpServletResponse);
        }else{
            try {
                veriticationCode(httpServletRequest);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }catch (VeriticationCodeException e){
                failureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse,e);
            }
        }
    }

    public void veriticationCode(HttpServletRequest httpServletRequest) throws VeriticationCodeException {
        String captcha = httpServletRequest.getParameter("captcha");
        String saveCaptcha = (String) httpServletRequest.getSession().getAttribute("captcha");
        if(StringUtils.isEmpty(captcha) || StringUtils.isEmpty(saveCaptcha) || !captcha.equals(saveCaptcha) ){
            throw new VeriticationCodeException();
        }
        if(!StringUtils.isEmpty(saveCaptcha)){
            httpServletRequest.getSession().removeAttribute("captcha");
        }
    }
}
