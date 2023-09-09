package searchengine.dto.indexing;

import lombok.Data;

@Data
public  class IndexingResponse {
    private boolean result;

    private String error;

    public static String START_INDEXING_ERROR = "Индексация уже запущена";
    public static String STOP_INDEXING_ERROR = "Индексация не запущена";

    public IndexingResponse(boolean result, String error) {
        this.result = result;
        this.error = error;
    }
}
