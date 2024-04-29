package searchengine.services.authentication;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import searchengine.dto.jwt.JwtAuthenticationResponse;
import searchengine.dto.sign.SignInRequest;
import searchengine.dto.sign.SignUpRequest;
import searchengine.exception.UserException;
import searchengine.model.Role;
import searchengine.model.User;
import searchengine.services.user.UserService;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(userService.getRole("USER"))
                .build();

        userService.create(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
//        refreshTokenRepository.add(user.getUsername(), refreshToken);

        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) throws UserException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
//        refreshTokenRepository.add(user.getUsername(), refreshToken);

        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    @Override
    public JwtAuthenticationResponse refresh(String refreshToken) {
        if (jwtService.validateRefreshToken(refreshToken)) {
            Claims refreshClaims = jwtService.getRefreshClaims(refreshToken);
            String username = refreshClaims.getSubject();
//            String saveRefreshToken = refreshTokenRepository.get(username);
//            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
            User user = userService.getByUsername(username);
            String accessToken = jwtService.generateAccessToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);
//                refreshTokenRepository.add(username, newRefreshToken);
            return new JwtAuthenticationResponse(accessToken, refreshToken);
//            }
        }
        return new JwtAuthenticationResponse(null, null);
    }
}
