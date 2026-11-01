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
@Table(name = "escale")
public class Escale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEscale;

    private String provenance;
    private String destination;
    private LocalDateTime dateEntree;
    private LocalDateTime dateSortie;
    private String statut;

    @ManyToOne
    @JoinColumn(name = "id_navire")
    private Navire navire;

    @ManyToOne
    @JoinColumn(name = "id_port")
    private Port port;

    @OneToMany(mappedBy = "escale")
    private List<Stationnement> stationnements;

    @OneToMany(mappedBy = "escale")
    private List<Mouillage> mouillages;

    @OneToMany(mappedBy = "escale")
    private List<Entree> entrees;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_entete_facture")
    private EnteteFacture enteteFacture;
}
