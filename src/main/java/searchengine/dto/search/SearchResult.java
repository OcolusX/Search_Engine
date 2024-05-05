package searchengine.dto.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Найденная страница")
public class SearchResult {

    @Schema(description = "URL сайта")
    private String site;

    @Schema(description = "Имя сайта")
    private String siteName;

    @Schema(description = "Путь страницы")
    private String uri;

    @Schema(description = "Заголовок страницы")
    private String title;

    @Schema(description = "Фрагмент текста, в котором найдены совпадения в формате HTML")
    private String snippet;

    @Schema(description = "Релевантность страницы")
    private float relevance;
}
