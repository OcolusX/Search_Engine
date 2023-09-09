package searchengine.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "lemmas")
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
