package ma.anp.facturation_droits_port.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.anp.facturation_droits_port.enums.TypeNavire;
import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.Unite;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="tarif")
public class Tarif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTarif;
    private String description;

    @ManyToOne
    @JoinColumn(name="id_marchandise")
    private Marchandise marchandise;

    @Enumerated(EnumType.STRING)
    private TypeOperation typeOperation;

    @Enumerated(EnumType.STRING)
    private TypeNavire typeNavire;

    @Enumerated(EnumType.STRING)
    private Unite unite;

    @ManyToOne
    @JoinColumn(name="id_port")
    private Port port;

    @OneToMany(mappedBy = "tarif")
    private List<TarifTranche> tarifTranches;

    @OneToMany(mappedBy = "tarif")
    private List<DetailFacture> detailFactures;
}
