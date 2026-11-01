package ma.anp.facturation_droits_port.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.anp.facturation_droits_port.enums.TypeNavire;

@Getter
@Setter
@NoArgsConstructor
public class NavireResponse {

    private Long idNavire;
    private String nom;
    private Double tjb;
    private TypeNavire typeNavire;
    private Double longueur;
    private Double largeur;
    private Double tirantEau;
    private String armateur;
}
