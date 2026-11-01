package ma.anp.facturation_droits_port.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ToucheeResponse {

    private Long idTouchees;
    private Long idNavire;
    private String nomNavire;
    private Long idPort;
    private String nomPort;
    private int annee;
    private int nombreTouchees;
}
