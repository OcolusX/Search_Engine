package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.Page;
import searchengine.model.Site;

import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {

    @Modifying
    @Query(value = "delete from Page p where p.site.id =:site_id")
    void deleteAllBySite_id(@Param("site_id") Integer siteId);

    @Query(value = "select p from Page p where p.path =:path and p.site.id =:site_id")
    Optional<Page> findByPathAndSite_id(@Param("path") String path, @Param("site_id") Integer siteId);
}
