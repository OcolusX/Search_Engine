package searchengine.model;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import lombok.Data;

@Entity
@Data
@Table(name = "lemmas", indexes = {@Index(name = "lemmaIndex", columnList = "site_id, lemma", unique = true)})
public class Lemma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false)
    private Site site;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String lemma;

    @Column(columnDefinition = "INT", nullable = false)
    private Integer frequency;
}
