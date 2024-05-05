package searchengine.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.dto.search.SearchResponse;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.model.User;
import searchengine.services.search.SearchService;
import searchengine.services.statistics.StatisticsService;
import searchengine.services.indexing.IndexingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api")
@Tag(name = "API")
public class ApiController {

    private final StatisticsService statisticsService;
    private final IndexingService indexingService;
    private final SearchService searchService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запрос статистики")
    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(statisticsService.getStatistics(user.getRole().getName().equals("ROLE_ADMIN")));
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запуск полной индексации")
    @GetMapping("/startIndexing")
    public ResponseEntity<IndexingResponse> startIndexing() {
        return ResponseEntity.ok(indexingService.startIndexing());
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Остановка текущей индексации")
    @GetMapping("/stopIndexing")
    public ResponseEntity<IndexingResponse> stopIndexing() {
        return ResponseEntity.ok(indexingService.stopIndexing());
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запуск индексации отдельной страницы")
    @PostMapping("/indexPage")
    public ResponseEntity<IndexingResponse> indexPage(@RequestParam("url") String url) {
        return ResponseEntity.ok(indexingService.indexPage(url));
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Поиск")
    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(
            @RequestParam("query") String query,
            @RequestParam(name = "site", defaultValue = "") String siteUrl,
            @RequestParam(name = "offset", defaultValue = "0") Integer offset,
            @RequestParam(name = "limit", defaultValue = "20") Integer limit) {
        return ResponseEntity.ok(searchService.search(query, siteUrl, offset, limit));
    }
}
