package searchengine.services.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.config.site.SitesList;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsData;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;
import searchengine.model.Site;
import searchengine.services.RepositoryService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final SitesList sites;
    private final RepositoryService repositoryService;

    @Override
    public StatisticsResponse getStatistics() {
        String[] errors = {
                "Ошибка индексации: главная страница сайта не доступна",
                "Ошибка индексации: сайт не доступен",
                ""
        };

        TotalStatistics total = new TotalStatistics();
        total.setSites((int) repositoryService.getSiteRepository().count());
        total.setIndexing(true);
        total.setPages((int) repositoryService.getPageRepository().count());
        total.setLemmas((int) repositoryService.getLemmaRepository().count());

        List<DetailedStatisticsItem> detailed = new ArrayList<>();
        List<Site> sitesList = repositoryService.getSiteRepository().findAll();
        for (Site site : sitesList) {
            DetailedStatisticsItem item = new DetailedStatisticsItem();
            item.setName(site.getName());
            item.setUrl(site.getUrl());

            Integer siteId = site.getId();
            int pages = (int) repositoryService.getPageRepository().countBySiteId(siteId);
            int lemmas = (int) repositoryService.getLemmaRepository().countBySiteId(siteId);

            item.setPages(pages);
            item.setLemmas(lemmas);
            item.setStatus(site.getStatus().name());
            // TODO исправить отображение ошибок
            item.setError(site.getLastError());

            // TODO исправить отображение даты и времени
            LocalDateTime statusTime = site.getStatusTime();
            ZonedDateTime zonedDateTime = statusTime.atZone(ZoneId.systemDefault());
            item.setStatusTime(zonedDateTime.toEpochSecond());

            detailed.add(item);
        }

        StatisticsResponse response = new StatisticsResponse();
        StatisticsData data = new StatisticsData();
        data.setTotal(total);
        data.setDetailed(detailed);
        response.setStatistics(data);
        response.setResult(true);
        return response;
    }
}
