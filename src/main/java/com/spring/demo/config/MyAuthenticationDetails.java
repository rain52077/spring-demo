package com.spring.demo.config;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
public class MyAuthenticationDetails extends WebAuthenticationDetails {
    private boolean imageCodeIsRight;

    public boolean isImageCodeIsRight() {
        return imageCodeIsRight;
    }

    public void setImageCodeIsRight(boolean imageCodeIsRight) {
        this.imageCodeIsRight = imageCodeIsRight;
    }

    public MyAuthenticationDetails(HttpServletRequest request) {
        super(request);
        String imageCode = request.getParameter("captcha");
        HttpSession session = request.getSession();
        String saveImageCode = (String) session.getAttribute("captcha");
        if(!StringUtils.isEmpty(saveImageCode)){
            session.removeAttribute("captcha");
        }
        if(!StringUtils.isEmpty(imageCode) && imageCode.equals(saveImageCode)){
            this.imageCodeIsRight = true;
        }
    }
}
