package searchengine.dto.indexing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ при запуске/остановке индексации")
public class IndexingResponse {

    @Schema(description = "Результат запуска/остановки индексации")
    private boolean result;

    @Schema(description = "Текст ошибки")
    private String error;

}
