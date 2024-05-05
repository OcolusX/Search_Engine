package searchengine.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ с токенами при аутентификации")
public class JwtAuthenticationResponse {

    @Schema(description = "Access токен")
    private String accessToken;

    @Schema(description = "Refresh токен")
    private String refreshToken;
}
