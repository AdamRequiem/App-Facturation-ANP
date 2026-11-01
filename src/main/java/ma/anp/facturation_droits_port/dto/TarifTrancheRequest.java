package ma.anp.facturation_droits_port.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TarifTrancheRequest {

    @PositiveOrZero(message = "La valeur minimale doit être supérieure ou égale à zéro")
    private Double valeurMin;

    @Positive(message = "La valeur maximale doit être positive")
    private Double valeurMax;

    @Positive(message = "Le montant doit être positif")
    private Double montant;

    @NotBlank(message = "L'unité est obligatoire")
    private String unite;

    private Long idTarif;
}
