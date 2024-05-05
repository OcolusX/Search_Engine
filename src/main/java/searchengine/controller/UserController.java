package searchengine.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import searchengine.model.User;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @GetMapping("/")
    public String user(@AuthenticationPrincipal User user, HttpSession session) {
        session.setAttribute("isAdmin", user.getRole().getName().equals("ROLE_ADMIN"));
        return "user/index";
    }

}
