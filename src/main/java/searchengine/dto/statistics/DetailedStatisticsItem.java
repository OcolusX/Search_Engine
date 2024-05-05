package searchengine.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Подробная статистика сайта")
public class DetailedStatisticsItem {

    @Schema(description = "URL сайта")
    private String url;

    @Schema(description = "Название сайта")
    private String name;

    @Schema(description = "Статус индексации")
    private String status;

    @Schema(description = "Время установления статуса индексации")
    private long statusTime;

    @Schema(description = "Текст ошибки")
    private String error;

    @Schema(description = "Кол-во проиндексированных страниц")
    private int pages;

    @Schema(description = "Кол-во найденных лемм")
    private int lemmas;
}
