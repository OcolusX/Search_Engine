package searchengine.dto.indexing;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.services.RepositoryService;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.RecursiveAction;

public class ParsingPageAction extends RecursiveAction {

    private final Site site;
    private final RepositoryService repositoryService;

    private final String path;

    private final List<ParsingPageAction> tasks = new LinkedList<>();


    public ParsingPageAction(Site site, String path, RepositoryService repositoryService) {
        this.site = site;
        this.path = path;
        this.repositoryService = repositoryService;
    }

    /**
     * Метод, обеспечивающий рекурсивный парсинг страниц данного сайта.
     */
    @Override
    protected void compute() {
        try {
            Thread.sleep(3000);
            // Получаем абсолютную ссылку для доступа к данной странице
            String url = site.getUrl() + path;

            Connection.Response response = getConnection(url).execute();
            Page page = new Page();
            page.setCode(response.statusCode());
            page.setPath(path);
            page.setSite(site);

            Document document = response.parse();
            page.setContent(document.toString());
            if (!repositoryService.savePage(page, site.getId()))
                return;
            Elements elements = document.select("a");
            for (Element element : elements) {
                String href = element.attr("href");
                if (!href.isEmpty() && href.charAt(0) == '/') {
                    ParsingPageAction parsingPageAction = new ParsingPageAction(site, href, repositoryService);
                    tasks.add(parsingPageAction);
                    parsingPageAction.fork();
                }
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (ParsingPageAction task : tasks) {
            task.join();
        }
    }

    /**
     * Обеспечивает соединение с сайтом по URL.
     *
     * @param url - абсолютная ссылка на данный сайт
     * @return - соединение, через которое можно получить полную информацию о сайте
     */
    private Connection getConnection(String url) {
        return Jsoup
                .connect(url)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com")
                .followRedirects(true);
    }
}
