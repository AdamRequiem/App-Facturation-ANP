package ma.anp.facturation_droits_port.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.anp.facturation_droits_port.enums.TypeRistourne;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MouillageResponse {

    private Long idMouillage;
    private LocalDateTime dateAccostage;
    private boolean abonnement;
    private boolean exoneration;
    private TypeRistourne typeRistourne;
    private Long idNavire;
    private String nomNavire;
    private Long idMarchandise;
    private String nomMarchandise;
    private Long idEscale;
}
