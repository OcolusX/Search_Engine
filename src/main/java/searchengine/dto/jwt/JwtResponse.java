package searchengine.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Универсальная форма ответа")
public class JwtResponse {

    @Schema(name = "Код ответа")
    private HttpStatus statusCode;

    @Schema(name = "Сообщение")
    private String message;

}
