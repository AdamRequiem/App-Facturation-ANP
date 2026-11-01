package ma.anp.facturation_droits_port.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ToucheeRequest {

    @NotNull(message = "L'id du navire est obligatoire")
    private Long idNavire;

    @NotNull(message = "L'id du port est obligatoire")
    private Long idPort;

    @Positive(message = "L'année doit être positive")
    private int annee;

    @PositiveOrZero(message = "Le nombre de touchées doit être positif ou zéro")
    private int nombreTouchees;
}
