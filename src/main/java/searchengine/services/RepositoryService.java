package searchengine.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.IndexingStatus;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Getter
public class RepositoryService {

    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;

    public RepositoryService(PageRepository pageRepository, SiteRepository siteRepository) {
        this.pageRepository = pageRepository;
        this.siteRepository = siteRepository;
    }

    /**
     * Удаляет из БД все сайты из списка и принадлежащие им страницы
     * @param siteList - список сайтов, которые необхожимо удалить из БД
     */
    @Transactional
    public void deleteAllBySiteList(List<Site> siteList) {
        for(Site site : siteList) {
            Integer id = site.getId();
            if(id != null) {
                pageRepository.deleteAllBySite_id(id);
                siteRepository.deleteById(id);
            }
        }
    }

    /**
     * Сохраняет данную страницу в БД и регистрирует данную информацию в таблице сайтов
     * @param page - страница, которую требуется сохранить
     * @param siteId - id сайта, к которому принадлежит страница
     * @return - true - страница сохранена успешно, false - данная страница уже имеется в БД
     */
    public synchronized boolean savePage(Page page, Integer siteId) {
        Optional<Page> byPath = pageRepository.findByPathAndSite_id(page.getPath(), siteId);
        if(byPath.isPresent() && byPath.get().getSite().getId().equals(siteId))
            return false;
        pageRepository.save(page);
        Optional<Site> byId = siteRepository.findById(siteId);
        if(byId.isPresent()) {
            Site site = byId.get();
            site.setStatusTime(LocalDateTime.now());
            siteRepository.save(site);
        }
        return true;
    }

    /**
     * Обновляет статус индексации и время установления этого статуса для данного сайта
     * @param site - сайт, для которого требуется обновить статус индексации
     * @param status - устанавливаемый статус индексации
     * @return - управляемый объект Site с обновлённым статусом
     */
    public Site updateStatusSite(Site site, IndexingStatus status) {
        site.setStatus(status);
        site.setStatusTime(LocalDateTime.now());
        return siteRepository.save(site);
    }

}
