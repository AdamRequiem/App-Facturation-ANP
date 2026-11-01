package ma.anp.facturation_droits_port.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="detail_facture")
public class DetailFacture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetailFacture;
    @Column(name = "type_droit")
    private String typeDroit;

    @Column(name = "description")
    private String description;

    // Malgré son nom, porte la quantité de l'unité affichée (jours, heures, m³...) selon l'opération.
    @Column(name = "quantite_heure")
    private Double quantiteHeure;

    @Column(name = "taux_horaire")
    private Double tauxHoraire;

    @Column(name = "unite")
    private String unite;

    @Column(name = "montant_ht")
    private Double montantHT;

    @ManyToOne
    @JoinColumn(name = "id_entree")
    private Entree entree;

    @ManyToOne
    @JoinColumn(name = "id_mouillage")
    private Mouillage mouillage;

    @ManyToOne
    @JoinColumn(name = "id_stationnement")
    private Stationnement stationnement;

    @ManyToOne
    @JoinColumn(name="id_enteteFacture")
    private EnteteFacture enteteFacture;

    @ManyToOne
    @JoinColumn(name="id_tarif")
    private Tarif tarif;

}
