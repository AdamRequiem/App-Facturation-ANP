package ma.anp.facturation_droits_port.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EscaleResponse {

    private Long idEscale;
    private String provenance;
    private String destination;
    private LocalDateTime dateEntree;
    private LocalDateTime dateSortie;
    private String statut;
    private Long idNavire;
    private String nomNavire;
    private Long idPort;
    private String nomPort;
    private Long idEnteteFacture;
    private Long numeroEnteteFacture;
}
