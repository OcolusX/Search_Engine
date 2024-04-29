package searchengine.model;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import lombok.Data;


@Entity
@Data
@Table(name = "pages", indexes = {@Index(name = "pathIndex", columnList = "site_id, path", unique = true)})
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false)
    private Site site;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String path;

    @Column(columnDefinition = "INT", nullable = false)
    private Integer code;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;


}
