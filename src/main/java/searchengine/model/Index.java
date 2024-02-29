package searchengine.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(
        name = "indexes",
        indexes = {@javax.persistence.Index(name = "page_lemma", columnList = "page_id, lemma_id", unique = true)}
)
public class Index {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", referencedColumnName = "id", nullable = false)
    private Page page;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lemma_id", referencedColumnName = "id", nullable = false)
    private Lemma lemma;

    @Column(name = "`rank`", columnDefinition = "FLOAT", nullable = false)
    private Float rank;
}
