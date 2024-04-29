package searchengine.services.authentication;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import searchengine.dto.jwt.JwtAuthenticationResponse;
import searchengine.dto.sign.SignInRequest;
import searchengine.dto.sign.SignUpRequest;

@Service
public interface AuthenticationService {

    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);

    JwtAuthenticationResponse refresh(String refreshToken);

}
