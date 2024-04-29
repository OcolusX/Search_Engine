package searchengine.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.jwt.JwtAuthenticationResponse;
import searchengine.dto.sign.SignInRequest;
import searchengine.dto.sign.SignUpRequest;
import searchengine.services.authentication.AuthenticationService;
import searchengine.services.authentication.JwtService;

@Controller
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute SignUpRequest request, HttpServletResponse response) {
        try {
            JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.signUp(request);

            response.addCookie(jwtService.createJwtAccessCookie(jwtAuthenticationResponse.getAccessToken()));
            response.addCookie(jwtService.createJwtRefreshCookie(jwtAuthenticationResponse.getRefreshToken()));

            return "redirect:/user/";

        } catch (RuntimeException e) {
            return "redirect:/sign-up";
        }
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public String signIn(@ModelAttribute SignInRequest request, HttpServletResponse response) {
        try {
            JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.signIn(request);

            response.addCookie(jwtService.createJwtAccessCookie(jwtAuthenticationResponse.getAccessToken()));
            response.addCookie(jwtService.createJwtRefreshCookie(jwtAuthenticationResponse.getRefreshToken()));

            return "redirect:/user/";
        } catch (RuntimeException e) {
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie accessCookie = jwtService.createJwtAccessCookie("");
        accessCookie.setMaxAge(0);
        response.addCookie(accessCookie);

        Cookie refreshCookie = jwtService.createJwtRefreshCookie("");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);

        return "redirect:/";
    }

    @Operation(summary = "Получить новый refresh токен")
    @GetMapping("/refresh")
    public String refresh(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if(cookies == null) {
            return "redirect:/";
        }

        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(JwtService.REFRESH_COOKIE_NAME)) {
                JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.refresh(cookie.getValue());
                response.addCookie(jwtService.createJwtAccessCookie(jwtAuthenticationResponse.getAccessToken()));
                response.addCookie(jwtService.createJwtRefreshCookie(jwtAuthenticationResponse.getRefreshToken()));
                return "redirect:/user/";
            }
        }

        return "redirect:/";
    }


}
