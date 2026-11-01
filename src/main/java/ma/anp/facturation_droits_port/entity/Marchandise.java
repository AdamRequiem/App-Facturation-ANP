package ma.anp.facturation_droits_port.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="marchandise")
public class Marchandise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMarchandise;
    private String nom;

    @OneToMany(mappedBy = "marchandise")
    private List<Stationnement> stationnements;

    @OneToMany(mappedBy = "marchandise")
    private List<Mouillage> mouillages;

    @OneToMany(mappedBy = "marchandise")
    private List<Tarif> tarifs;
}
