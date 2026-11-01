package ma.anp.facturation_droits_port.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.anp.facturation_droits_port.enums.RaisonEntree;
import ma.anp.facturation_droits_port.enums.TypeRistourne;

@Getter
@Setter
@NoArgsConstructor
public class EntreeResponse {

    private Long idEntree;
    private RaisonEntree raison;
    private boolean facturationNext;
    private boolean exoneration;
    private TypeRistourne typeRistourne;
    private Long idNavire;
    private String nomNavire;
    private Long idEscale;
}
