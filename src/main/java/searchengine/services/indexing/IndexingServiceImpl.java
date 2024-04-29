package searchengine.services.indexing;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import searchengine.config.site.SitesList;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.parsing.ParsingPageAction;
import searchengine.model.IndexingStatus;
import searchengine.model.Site;
import searchengine.services.RepositoryService;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class IndexingServiceImpl implements IndexingService {

    Logger logger = LoggerFactory.getLogger(IndexingServiceImpl.class);

    private ExecutorService executorService;
    private final RepositoryService repositoryService;
    private final SitesList sitesList;
    private final List<ForkJoinPool> pools;
    private boolean indexing = false;


    /**
     * Запускает рекурсивный обход в многопоточном режиме всех страниц сайтов, прописанных в файле конфигурации
     *
     * @return -    IndexingResponse({result : true, error : ""}) - успешный запуск обхода;
     *              IndexingResponse({result : false, error : "Индексация уже запущена"}) - обход уже запущен.
     */
    @Override
    public IndexingResponse startIndexing() {
        if(indexing) {
            return new IndexingResponse(false, IndexingResponse.START_INDEXING_ERROR);
        }

        indexing = true;
        List<Site> sites = getSites();
        repositoryService.deleteAllBySiteList(sites);

        executorService = Executors.newFixedThreadPool(sites.size());
        for (Site site : sites) {
            Site finalSite = repositoryService.updateStatusSite(site, IndexingStatus.INDEXING);
            executorService.submit(() -> {
                ForkJoinPool parsingPagePool = new ForkJoinPool();
                try {
                    ParsingPageAction parsingPageAction = new ParsingPageAction(finalSite, "/", repositoryService);
                    pools.add(parsingPagePool);
                    parsingPagePool.invoke(parsingPageAction);
                    repositoryService.updateStatusSite(finalSite, IndexingStatus.INDEXED);
                } catch (RuntimeException e) {
                    //TODO сделать нормальную обработку исключений
                    e.printStackTrace();
                    if(indexing) {
                        finalSite.setLastError(e.getMessage());
                        repositoryService.updateStatusSite(finalSite, IndexingStatus.FAILED);
                    }
                }
                pools.remove(parsingPagePool);
                if(pools.isEmpty()) {
                        indexing = false;
                }
            });
        }
        return new IndexingResponse(true, "");
    }

    @Override
    public IndexingResponse stopIndexing() {
        if (!indexing) {
            return new IndexingResponse(false, IndexingResponse.STOP_INDEXING_ERROR);
        }
        indexing = false;
        for(ForkJoinPool pool : pools) {
            pool.shutdownNow();
        }
        executorService.shutdownNow();
        for(Site site : getSites()) {
            if(site.getStatus() != IndexingStatus.INDEXED) {
                site.setLastError("Индексация остановлена пользователем");
                repositoryService.updateStatusSite(site, IndexingStatus.FAILED);
            }
        }

        return new IndexingResponse(true, "");
    }

    @Override
    public IndexingResponse indexPage(String url) {
        List<Site> sites = getSites();
        for(Site site : sites) {
            Matcher matcher = Pattern.compile(site.getUrl()).matcher(url);
            if(matcher.find()) {
                site = repositoryService.updateStatusSite(site, IndexingStatus.INDEXING);
                String path = url.substring(matcher.end() - 1);
                repositoryService.deletePage(path, site.getId());

                ParsingPageAction parsingPageAction = new ParsingPageAction(site, path, repositoryService, false);
                parsingPageAction.fork();
                parsingPageAction.join();
            }
        }
        return new IndexingResponse(true, "");
    }


    private List<Site> getSites() {
        List<Site> sites = new LinkedList<>();
        for (searchengine.config.site.Site configSite : sitesList.getSites()) {
                Site site = new Site();
                site.setName(configSite.getName());
                site.setUrl(configSite.getUrl());
                sites.add(site);
        }
        return sites;
    }

    public boolean isIndexing() {
        return indexing;
    }
}
