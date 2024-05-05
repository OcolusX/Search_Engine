package searchengine.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Статистика содержащихся страниц")
public class StatisticsResponse {

    @Schema(description = "Результат запроса статистики")
    private boolean result;

    @Schema(description = "Статистика")
    private StatisticsData statistics;
}
