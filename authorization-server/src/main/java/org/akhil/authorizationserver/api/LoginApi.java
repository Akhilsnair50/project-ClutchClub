package org.akhil.authorizationserver.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginApi {
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
