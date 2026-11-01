package ma.anp.facturation_droits_port.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "entete_facture")
public class EnteteFacture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEnteteFacture;
    private Long numero;
    private LocalDateTime date;
    private String client;
    @Column(name = "total_ht")
    private Double totalHT;

    @Column(name = "taux_tva")
    private Double taux_TVA;

    @Column(name = "montant_tva")
    private Double montant_TVA;

    @Column(name = "total_ttc")
    private Double totalTTC;
    private String statut;

    @OneToOne(mappedBy = "enteteFacture")
    private Escale escale;

    @OneToMany(mappedBy = "enteteFacture")
    private List<DetailFacture> detailFactures;
}
