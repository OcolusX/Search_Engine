package searchengine.dto.search;

import lombok.Data;

import java.util.List;

@Data
public class SearchResponse {
    private boolean result;
    private int count;
    private List<SearchResult> data;
    private String error;

    public SearchResponse(boolean result, String error) {
        this.result = result;
        this.error = error;
    }
}
