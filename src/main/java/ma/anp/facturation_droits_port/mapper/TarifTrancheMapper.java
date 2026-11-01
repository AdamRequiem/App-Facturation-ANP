package ma.anp.facturation_droits_port.mapper;

import ma.anp.facturation_droits_port.dto.TarifTrancheRequest;
import ma.anp.facturation_droits_port.dto.TarifTrancheResponse;
import ma.anp.facturation_droits_port.entity.Tarif;
import ma.anp.facturation_droits_port.entity.TarifTranche;
import org.springframework.stereotype.Component;

@Component
public class TarifTrancheMapper {

    public TarifTranche toEntity(TarifTrancheRequest request) {
        TarifTranche tarifTranche = new TarifTranche();
        tarifTranche.setValeurMin(request.getValeurMin());
        tarifTranche.setValeurMax(request.getValeurMax());
        tarifTranche.setMontant(request.getMontant());
        tarifTranche.setUnite(request.getUnite());

        if (request.getIdTarif() != null) {
            Tarif tarif = new Tarif();
            tarif.setIdTarif(request.getIdTarif());
            tarifTranche.setTarif(tarif);
        }

        return tarifTranche;
    }

    public TarifTrancheResponse toResponse(TarifTranche tarifTranche) {
        TarifTrancheResponse response = new TarifTrancheResponse();
        response.setIdTarifTranche(tarifTranche.getIdTarifTranche());
        response.setValeurMin(tarifTranche.getValeurMin());
        response.setValeurMax(tarifTranche.getValeurMax());
        response.setMontant(tarifTranche.getMontant());
        response.setUnite(tarifTranche.getUnite());

        if (tarifTranche.getTarif() != null) {
            response.setIdTarif(tarifTranche.getTarif().getIdTarif());
        }

        return response;
    }
}
