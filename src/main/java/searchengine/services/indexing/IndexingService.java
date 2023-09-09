package searchengine.services.indexing;

import org.springframework.stereotype.Service;
import searchengine.dto.indexing.IndexingResponse;

@Service
public interface IndexingService {

    /**
     * Запускает обход всех страниц сайтов, прописанных в файле конфигурации
     *
     * @return -    IndexingResponse({result : true, error : ""}) - успешный запуск обхода,
     *              IndexingResponse({result : false, error : "Индексация уже запущена"}) - обход уже запущен.
     */
    IndexingResponse startIndexing();

    IndexingResponse stopIndexing();
}
