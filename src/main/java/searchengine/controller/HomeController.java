package searchengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
