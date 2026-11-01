package ma.anp.facturation_droits_port.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.anp.facturation_droits_port.enums.TypeNavire;
import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.Unite;

@Getter
@Setter
@NoArgsConstructor
public class TarifResponse {

    private Long idTarif;
    private String description;
    private TypeOperation typeOperation;
    private TypeNavire typeNavire;
    private Unite unite;
    private Long idMarchandise;
    private String nomMarchandise;
    private Long idPort;
    private String nomPort;
}
