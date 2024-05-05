package searchengine.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Статистика")
public class StatisticsData {

    @Schema(description = "Глобальная статистика")
    private TotalStatistics total;

    @Schema(description = "Подробная статистика по каждому сайту")
    private List<DetailedStatisticsItem> detailed;
}
