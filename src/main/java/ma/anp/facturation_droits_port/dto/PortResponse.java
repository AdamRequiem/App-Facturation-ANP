package ma.anp.facturation_droits_port.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.anp.facturation_droits_port.enums.TypeTerminal;

@Getter
@Setter
@NoArgsConstructor
public class PortResponse {

    private Long idPort;
    private String nom;
    private TypeTerminal typeTerminal;
}
