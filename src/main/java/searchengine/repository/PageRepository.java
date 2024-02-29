package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.Page;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {

    @Query(value = "select p from Page p where p.site.id =:site_id")
    List<Page> findAllBySiteId(@Param("site_id") Integer siteId);

    @Modifying
    @Query(value = "delete from Page p where p.site.id =:site_id")
    void deleteAllBySiteId(@Param("site_id") Integer siteId);

    @Query(value = "select p from Page p where p.site.id =:site_id and p.path =:path")
    Optional<Page> findByPathAndSiteId(@Param("path") String path, @Param("site_id") Integer siteId);

    long countBySiteId(Integer siteId);
}
