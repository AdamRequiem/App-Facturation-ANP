package ma.anp.facturation_droits_port.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DetailFactureRequest {

    @NotBlank(message = "Le type de droit est obligatoire")
    private String typeDroit;

    private String description;

    @Positive(message = "La quantité doit être positive")
    private Double quantiteHeure;

    @Positive(message = "Le taux horaire doit être positif")
    private Double tauxHoraire;

    private String unite;

    @Positive(message = "Le montant HT doit être positif")
    private Double montantHT;

    private Long idEntree;
    private Long idMouillage;
    private Long idStationnement;
    private Long idEnteteFacture;
    private Long idTarif;
}
