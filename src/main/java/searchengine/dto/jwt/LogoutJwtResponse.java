package searchengine.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Результат выхода из системы")
public class LogoutJwtResponse {

    @Schema(description = "Код ответа")
    private HttpStatus statusCode;

    @Schema(description = "Сообщение")
    private String message;

}
