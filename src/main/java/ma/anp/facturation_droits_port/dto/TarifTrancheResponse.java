package ma.anp.facturation_droits_port.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TarifTrancheResponse {

    private Long idTarifTranche;
    private Double valeurMin;
    private Double valeurMax;
    private Double montant;
    private String unite;
    private Long idTarif;
}
