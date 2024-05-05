package searchengine.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Глобальная статистика")
public class TotalStatistics {

    @Schema(description = "Кол-во сайтов")
    private int sites;

    @Schema(description = "Кол-во страниц")
    private int pages;

    @Schema(description = "Кол-во лемм")
    private int lemmas;

    @Schema(description = "Индексация запущена/не запущена")
    private boolean indexing;
}
