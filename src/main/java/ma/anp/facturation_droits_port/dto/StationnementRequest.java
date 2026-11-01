package ma.anp.facturation_droits_port.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.anp.facturation_droits_port.enums.TypeRistourne;

@Getter
@Setter
@NoArgsConstructor
public class StationnementRequest {

    private boolean embarquement;
    private boolean abonnement;
    private boolean exoneration;
    private TypeRistourne typeRistourne;

    private Long idNavire;
    private Long idMarchandise;
    private Long idEscale;
}
