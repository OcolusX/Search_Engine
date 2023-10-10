package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import searchengine.model.Lemma;

import java.util.List;
import java.util.Optional;

public interface LemmaRepository extends JpaRepository<Lemma, Integer> {
    Optional<Lemma> findByLemma(String lemma);

    @Modifying
    @Query(value = "delete from Lemma l where l.site.id =:site_id")
    void deleteAllBySiteId(@Param("site_id") Integer siteId);
}
