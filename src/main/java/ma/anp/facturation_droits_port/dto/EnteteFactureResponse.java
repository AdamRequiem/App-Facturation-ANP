package ma.anp.facturation_droits_port.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EnteteFactureResponse {

    private Long idEnteteFacture;
    private Long numero;
    private LocalDateTime date;
    private String client;
    private Double totalHT;
    private Double taux_TVA;
    private Double montant_TVA;
    private Double totalTTC;
    private String statut;
    private Long idEscale;
    private List<DetailFactureResponse> details;
}
