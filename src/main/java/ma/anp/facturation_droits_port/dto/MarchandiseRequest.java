package ma.anp.facturation_droits_port.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MarchandiseRequest {

    @NotBlank(message = "Le nom de la marchandise est obligatoire")
    private String nom;
}
