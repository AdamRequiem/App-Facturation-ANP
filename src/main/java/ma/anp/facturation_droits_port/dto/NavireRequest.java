package ma.anp.facturation_droits_port.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.anp.facturation_droits_port.enums.TypeNavire;

@Getter
@Setter
@NoArgsConstructor
public class NavireRequest {

    @NotBlank(message = "Le nom du navire est obligatoire")
    private String nom;

    @NotNull(message = "Le TJB est obligatoire")
    @Positive(message = "Le TJB doit être positif")
    private Double tjb;

    @NotNull(message = "Le type de navire est obligatoire")
    private TypeNavire typeNavire;

    @Positive(message = "La longueur doit être positive")
    private Double longueur;

    @Positive(message = "La largeur doit être positive")
    private Double largeur;

    @Positive(message = "Le tirant d'eau doit être positif")
    private Double tirantEau;

    @NotBlank(message = "L'armateur est obligatoire")
    private String armateur;
}
