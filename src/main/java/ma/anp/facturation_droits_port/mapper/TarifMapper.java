package ma.anp.facturation_droits_port.mapper;

import ma.anp.facturation_droits_port.dto.TarifRequest;
import ma.anp.facturation_droits_port.dto.TarifResponse;
import ma.anp.facturation_droits_port.entity.Marchandise;
import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.entity.Tarif;
import org.springframework.stereotype.Component;

@Component
public class TarifMapper {

    public Tarif toEntity(TarifRequest request) {
        Tarif tarif = new Tarif();
        tarif.setDescription(request.getDescription());
        tarif.setTypeOperation(request.getTypeOperation());
        tarif.setTypeNavire(request.getTypeNavire());
        tarif.setUnite(request.getUnite());

        if (request.getIdMarchandise() != null) {
            Marchandise marchandise = new Marchandise();
            marchandise.setIdMarchandise(request.getIdMarchandise());
            tarif.setMarchandise(marchandise);
        }

        if (request.getIdPort() != null) {
            Port port = new Port();
            port.setIdPort(request.getIdPort());
            tarif.setPort(port);
        }

        return tarif;
    }

    public TarifResponse toResponse(Tarif tarif) {
        TarifResponse response = new TarifResponse();
        response.setIdTarif(tarif.getIdTarif());
        response.setDescription(tarif.getDescription());
        response.setTypeOperation(tarif.getTypeOperation());
        response.setTypeNavire(tarif.getTypeNavire());
        response.setUnite(tarif.getUnite());

        if (tarif.getMarchandise() != null) {
            response.setIdMarchandise(tarif.getMarchandise().getIdMarchandise());
            response.setNomMarchandise(tarif.getMarchandise().getNom());
        }

        if (tarif.getPort() != null) {
            response.setIdPort(tarif.getPort().getIdPort());
            response.setNomPort(tarif.getPort().getNom());
        }

        return response;
    }
}
