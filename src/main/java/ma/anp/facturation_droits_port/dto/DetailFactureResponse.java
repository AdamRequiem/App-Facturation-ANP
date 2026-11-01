package ma.anp.facturation_droits_port.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DetailFactureResponse {

    private Long idDetailFacture;
    private String typeDroit;
    private String description;
    private Double quantiteHeure;
    private Double tauxHoraire;
    private String unite;
    private Double montantHT;
    private Long idEntree;
    private Long idMouillage;
    private Long idStationnement;
    private Long idEnteteFacture;
    private Long idTarif;
}
