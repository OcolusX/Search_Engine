package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import searchengine.model.Lemma;
import searchengine.model.Page;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LemmaRepository extends JpaRepository<Lemma, Integer> {
    @Query(value = "select l from Lemma l where l.site.id =:site_id and l.lemma =:lemma")
    Optional<Lemma> findByLemmaAndSiteId(@Param("lemma") String lemma, @Param("site_id") Integer siteId);

//    @Query(value = "select l from Lemma l where l.site_id =:site_id and l.lemma in :lemmas order by l.lemma")
//    List<Lemma> findAllByLemmasListAndSiteId(@Param("lemmas") List<String> lemmas, @Param("site_id") Integer siteId);

    @Query(value = "select l from Lemma l where l.lemma in :lemmas order by l.lemma")
    List<Lemma> findAllByLemmasList(@Param("lemmas") Collection<String> lemmas);

    @Modifying
    @Query(value = "delete from Lemma l where l.site.id =:site_id")
    void deleteAllBySiteId(@Param("site_id") Integer siteId);

    long countBySiteId(Integer siteId);
}
