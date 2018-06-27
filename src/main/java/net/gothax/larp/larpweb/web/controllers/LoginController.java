package net.gothax.larp.larpweb.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String onLogin() {
        return "login";
    }

    @RequestMapping("/accessDenied")
    public String onAccessDenied() {
        return "accessDenied";
    }
}
