package ma.anp.facturation_droits_port.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EscaleRequest {

    @NotBlank(message = "La provenance est obligatoire")
    private String provenance;

    @NotBlank(message = "La destination est obligatoire")
    private String destination;

    @NotNull(message = "La date d'entrée est obligatoire")
    private LocalDateTime dateEntree;

    private LocalDateTime dateSortie;

    @NotBlank(message = "Le statut est obligatoire")
    private String statut;

    @NotNull(message = "L'id du navire est obligatoire")
    private Long idNavire;

    @NotNull(message = "L'id du port est obligatoire")
    private Long idPort;

    private Long idEnteteFacture;
}
