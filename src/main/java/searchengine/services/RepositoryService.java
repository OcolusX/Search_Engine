package searchengine.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.*;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Getter
public class RepositoryService {

    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;

    private final LemmaRepository lemmaRepository;

    private final IndexRepository indexRepository;

    public RepositoryService(PageRepository pageRepository,
                             SiteRepository siteRepository,
                             LemmaRepository lemmaRepository,
                             IndexRepository indexRepository) {
        this.pageRepository = pageRepository;
        this.siteRepository = siteRepository;
        this.lemmaRepository = lemmaRepository;
        this.indexRepository = indexRepository;
    }

    /**
     * Удаляет из БД все сайты из списка и принадлежащие им страницы
     *
     * @param siteList - список сайтов, которые необхожимо удалить из БД
     */
    @Transactional
    public void deleteAllBySiteList(List<Site> siteList) {
        for (Site site : siteList) {
            Optional<Site> byName = siteRepository.findByName(site.getName());
            if (byName.isPresent()) {
                Integer id = byName.get().getId();
                List<Page> pages = pageRepository.findAllBySiteId(id);
                pages.forEach(page -> indexRepository.deleteAllByPageId(page.getId()));
                pageRepository.deleteAllBySiteId(id);
                lemmaRepository.deleteAllBySiteId(id);
                siteRepository.deleteById(id);
            }
        }
    }

    @Transactional
    public void deleteAllByPagePathAndSiteId(String path, Integer siteId) {
        Optional<Page> byPathAndSiteId = pageRepository.findByPathAndSiteId(path, siteId);
        byPathAndSiteId.ifPresent(value -> indexRepository.deleteAllByPageId(value.getId()));
    }

    /**
     * Сохраняет данную страницу в БД и регистрирует данную информацию в таблице сайтов
     *
     * @param page   - страница, которую требуется сохранить
     * @param siteId - id сайта, к которому принадлежит страница
     * @return - true - страница сохранена успешно, false - данная страница уже имеется в БД
     */
    @Transactional
    public synchronized Page savePage(Page page, Integer siteId) {
        Optional<Page> byPath = pageRepository.findByPathAndSiteId(page.getPath(), siteId);
        if(byPath.isPresent()) {
            return byPath.get();
        }
        page = pageRepository.save(page);
        Optional<Site> byId = siteRepository.findById(siteId);
        if (byId.isPresent()) {
            Site site = byId.get();
            site.setStatusTime(LocalDateTime.now());
            siteRepository.save(site);
        }
        return page;
    }

    @Transactional
    public synchronized boolean isPageExists(String path, Integer siteId) {
        Optional<Page> byPathAndSiteId = pageRepository.findByPathAndSiteId(path, siteId);
        return byPathAndSiteId.isPresent();
    }

    /**
     * Обновляет статус индексации и время установления этого статуса для данного сайта
     *
     * @param site   - сайт, для которого требуется обновить статус индексации
     * @param status - устанавливаемый статус индексации
     * @return - управляемый объект Site с обновлённым статусом
     */
    public Site updateStatusSite(Site site, IndexingStatus status) {
        site.setStatus(status);
        site.setStatusTime(LocalDateTime.now());
        return siteRepository.save(site);
    }

    @Transactional
    public synchronized void saveLemmasMap(Map<String, Integer> lemmasMap, Page page) {
        for (String key : lemmasMap.keySet()) {
            Float rank = lemmasMap.get(key) + 0f;
            Optional<Lemma> byLemma = lemmaRepository.findByLemma(key);
            if (byLemma.isPresent()) {
                Lemma lemma = byLemma.get();

                Optional<Index> byPageIdAndLemmaId = indexRepository.findByPageIdAndLemmaId(page.getId(), lemma.getId());
                if (byPageIdAndLemmaId.isPresent()) {
                    Index index = byPageIdAndLemmaId.get();
                    if (!index.getRank().equals(rank)) {
                        index.setRank(rank);
                        indexRepository.save(index);
                    }
                } else {
                    lemma.setFrequency(lemma.getFrequency() + 1);
                    lemma = lemmaRepository.save(lemma);

                    Index index = new Index();
                    index.setPage(page);
                    index.setLemma(lemma);
                    index.setRank(rank);
                    indexRepository.save(index);
                }
            } else {
                Lemma lemma = new Lemma();
                lemma.setSite(page.getSite());
                lemma.setLemma(key);
                lemma.setFrequency(1);
                lemma = lemmaRepository.save(lemma);

                Index index = new Index();
                index.setPage(page);
                index.setLemma(lemma);
                index.setRank(rank);
                indexRepository.save(index);
            }
        }
    }

}
