package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import searchengine.model.Index;

import java.util.Optional;

public interface IndexRepository extends JpaRepository<Index, Integer> {

    @Modifying
    @Query(value = "delete from Index i where i.page.id =:page_id")
    void deleteAllByPageId(@Param("page_id") Integer pageId);

    @Query(value = "select i from Index i where i.page.id =:page_id and i.lemma.id =:lemma_id")
    Optional<Index> findByPageIdAndLemmaId(@Param("page_id") Integer pageId, @Param("lemma_id") Integer lemmaId);

    @Query(value = "select count(i) from Index i where i.lemma.id =:lemma_id")
    Long countByLemmaId(@Param("lemma_id") Integer lemmaId);
}
