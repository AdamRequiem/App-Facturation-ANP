package ma.anp.facturation_droits_port.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.anp.facturation_droits_port.enums.TypeNavire;
import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.Unite;

@Getter
@Setter
@NoArgsConstructor
public class TarifRequest {

    private String description;

    @NotNull(message = "Le type d'opération est obligatoire")
    private TypeOperation typeOperation;

    private TypeNavire typeNavire;

    private Unite unite;

    private Long idMarchandise;
    private Long idPort;
}
