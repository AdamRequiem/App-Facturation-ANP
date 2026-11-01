package ma.anp.facturation_droits_port.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EnteteFactureRequest {

    @NotNull(message = "Le numéro de facture est obligatoire")
    @Positive(message = "Le numéro de facture doit être positif")
    private Long numero;

    private LocalDateTime date;

    @NotBlank(message = "Le client est obligatoire")
    private String client;

    @Positive(message = "Le total HT doit être positif")
    private Double totalHT;

    private Double taux_TVA;
    private Double montant_TVA;

    @Positive(message = "Le total TTC doit être positif")
    private Double totalTTC;

    private String statut;

    private Long idEscale;
}
