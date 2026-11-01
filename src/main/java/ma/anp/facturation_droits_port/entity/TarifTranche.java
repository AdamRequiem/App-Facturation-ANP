package ma.anp.facturation_droits_port.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="tarif_tranche")
public class TarifTranche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTarifTranche;
    private Double valeurMin;
    private Double valeurMax;
    private Double montant;
    private String unite;

    @ManyToOne
    @JoinColumn(name="id_tarif")
    private Tarif tarif;

}
