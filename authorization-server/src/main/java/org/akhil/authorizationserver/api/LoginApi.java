package org.akhil.authorizationserver.api;

import lombok.RequiredArgsConstructor;
import org.akhil.authorizationserver.dto.CreateAppUserDto;
import org.akhil.authorizationserver.service.user.impl.UserServiceImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LoginApi {

    private final UserServiceImpl userService;
    @GetMapping("/login")
    public String login() {
        return "login1";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    @PostMapping("/logout")
    public String logoutOK(HttpSecurity http) throws Exception {
        http.logout(logout -> logout.deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true));
        return "login?logout";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        List<String> roles = List.of("ROLE_USER");
        model.addAttribute("user", new CreateAppUserDto("","",roles ,""));
        return "register1";
    }
    @PostMapping("/register")
    public String registerNewUser(CreateAppUserDto userDto){
        userService.saveUser(userDto);

        return "redirect:/login";
    }
}
