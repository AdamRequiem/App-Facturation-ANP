package ma.anp.facturation_droits_port.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.anp.facturation_droits_port.enums.RaisonEntree;
import ma.anp.facturation_droits_port.enums.TypeRistourne;

@Getter
@Setter
@NoArgsConstructor
public class EntreeRequest {

    private RaisonEntree raison;

    private boolean facturationNext;
    private boolean exoneration;
    private TypeRistourne typeRistourne;

    private Long idNavire;
    private Long idEscale;
}
