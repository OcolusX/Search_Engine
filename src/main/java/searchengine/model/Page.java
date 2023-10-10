package searchengine.model;

import lombok.Data;

import javax.persistence.*;
import javax.persistence.Index;

@Entity
@Data
@Table(name = "pages", indexes = {@Index(name = "pathIndex", columnList = "path", unique = true)})
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false)
    private Site site;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String path;

    @Column(columnDefinition = "INT", nullable = false)
    private Integer code;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;


}
