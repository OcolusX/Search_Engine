package searchengine.dto.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Результат поискового запроса")
public class SearchResponse {

    @Schema(description = "Результат поиска")
    private boolean result;

    @Schema(description = "Кол-во найденных страниц")
    private int count;

    @Schema(description = "Список найденных страниц")
    private List<SearchResult> data;

    @Schema(description = "Текст ошибка")
    private String error;

    public SearchResponse(boolean result, String error) {
        this.result = result;
        this.error = error;
    }
}
