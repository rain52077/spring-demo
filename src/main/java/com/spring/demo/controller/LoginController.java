package com.spring.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @RequestMapping(value = "/login")
    public String login(Model modelMap){
        modelMap.addAttribute("username","admin");
        modelMap.addAttribute("password","123456");
        return "myLogin";
    }
    @RequestMapping(value = "/auth/form" ,method = RequestMethod.POST)
    public String authForm(@RequestParam("username") String username,
                         @RequestParam("password") String password){

        System.out.println("========登录成功======="+username+"----"+password);
        return "success";
    }
}
