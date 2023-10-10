package searchengine.controllers;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.RepositoryService;
import searchengine.services.StatisticsService;
import searchengine.services.indexing.IndexingService;
import searchengine.services.indexing.IndexingServiceImpl;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StatisticsService statisticsService;
    private final RepositoryService repositoryService;

    private final IndexingService indexingService;

    public ApiController(StatisticsService statisticsService, RepositoryService repositoryService, IndexingService indexingService) {
        this.statisticsService = statisticsService;
        this.repositoryService = repositoryService;
        this.indexingService = indexingService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<IndexingResponse> startIndexing() {
        return ResponseEntity.ok(indexingService.startIndexing());
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<IndexingResponse> stopIndexing() {
        return ResponseEntity.ok(indexingService.stopIndexing());
    }

//    @PostMapping("/indexPage")
//    public ResponseEntity<IndexingResponse> indexPage(@RequestParam("url") String url) {
//
//    }
}
