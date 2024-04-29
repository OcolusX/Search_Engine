package searchengine.services.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.parsing.HtmlLemmaFinder;
import searchengine.dto.search.SearchResponse;
import searchengine.model.Index;
import searchengine.model.Lemma;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.services.RepositoryService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final RepositoryService repositoryService;

    @Override
    public SearchResponse search(String query, String siteUrl, Integer offset, Integer limit) {
        Integer siteId = -1;
        if (!siteUrl.isBlank()) {
            Optional<Site> byUrl = repositoryService.getSiteRepository().findByUrl(siteUrl);
            if (byUrl.isPresent()) {
                siteId = byUrl.get().getId();
            } else {
                //TODO добавить ошибку
                return new SearchResponse(false, "");
            }
        }

        List<String> lemmas;
        try {
            HtmlLemmaFinder finder = HtmlLemmaFinder.getInstance();
            lemmas = new LinkedList<>(finder.getLemmasSet(query));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Lemma> lemmaList = new LinkedList<>();
        if (siteId == -1) {
            lemmaList.addAll(repositoryService.getLemmaRepository().findAllByLemmasList(lemmas));
        } else {
            lemmaList.addAll(repositoryService.getLemmaRepository().findAllByLemmasListAndSiteId(lemmas, siteId));
        }

        //TODO исправить систему поиска
        List<Index> indexList = repositoryService.getIndexRepository().findAllByLemma(lemmaList.get(0));
        List<Page> pageList = indexList.stream().map(Index::getPage).toList();
        Iterator<Lemma> iterator = lemmaList.iterator();
        iterator.next();
        while(iterator.hasNext()) {
            indexList = indexList.stream()
                    .filter(i -> i.getLemma().getId().equals(iterator.next().getId()))
                    .collect(Collectors.toList());
        }

        //TODO добавить ошибку
        if(indexList.isEmpty()) {
            return new SearchResponse(false, "");
        }

        for(Index i : indexList) {
            Page page = i.getPage();

        }


        return null;
    }
}
