package com.spring.demo.exception;


import org.springframework.security.core.AuthenticationException;

public class VeriticationCodeException extends AuthenticationException {
    public VeriticationCodeException(){
        super("图形验证码校验失败");
    }
}
