package searchengine.rest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import searchengine.dto.jwt.JwtAuthenticationResponse;
import searchengine.dto.sign.SignInRequest;
import searchengine.exception.UserException;
import searchengine.services.authentication.AuthenticationService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/sign-up")
    public String register() {
        return "sign-up";
    }


}
