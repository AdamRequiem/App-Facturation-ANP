package ma.anp.facturation_droits_port.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.anp.facturation_droits_port.enums.TypeNavire;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="navire")
public class Navire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNavire;
    private String nom;
    private Double tjb;
    private Double longueur;
    private Double largeur;
    private Double tirantEau;
    private String armateur;

    @Enumerated(EnumType.STRING)
    private TypeNavire typeNavire;

    @OneToMany(mappedBy = "navire")
    private List<Escale> escales;

    @OneToMany(mappedBy = "navire")
    private List<Touchee> touchees;

    public boolean isTransbordeur() {
        if(typeNavire==TypeNavire.TRANSBORDEUR) {
            return true;
        }
        else {
            return false;
        }
    }
}
