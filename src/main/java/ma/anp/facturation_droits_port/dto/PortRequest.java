package ma.anp.facturation_droits_port.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.anp.facturation_droits_port.enums.TypeTerminal;

@Getter
@Setter
@NoArgsConstructor
public class PortRequest {

    @NotBlank(message = "Le nom du port est obligatoire")
    private String nom;

    @NotNull(message = "Le type de terminal est obligatoire")
    private TypeTerminal typeTerminal;
}
