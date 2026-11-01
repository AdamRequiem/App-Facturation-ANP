package ma.anp.facturation_droits_port.mapper;

import ma.anp.facturation_droits_port.dto.EnteteFactureRequest;
import ma.anp.facturation_droits_port.dto.EnteteFactureResponse;
import ma.anp.facturation_droits_port.entity.EnteteFacture;
import ma.anp.facturation_droits_port.entity.Escale;
import org.springframework.stereotype.Component;

@Component
public class EnteteFactureMapper {

    private final DetailFactureMapper detailFactureMapper;

    public EnteteFactureMapper(DetailFactureMapper detailFactureMapper) {
        this.detailFactureMapper = detailFactureMapper;
    }

    public EnteteFacture toEntity(EnteteFactureRequest request) {
        EnteteFacture enteteFacture = new EnteteFacture();
        enteteFacture.setNumero(request.getNumero());
        enteteFacture.setDate(request.getDate());
        enteteFacture.setClient(request.getClient());
        enteteFacture.setTotalHT(request.getTotalHT());
        enteteFacture.setTaux_TVA(request.getTaux_TVA());
        enteteFacture.setMontant_TVA(request.getMontant_TVA());
        enteteFacture.setTotalTTC(request.getTotalTTC());
        enteteFacture.setStatut(request.getStatut());

        if (request.getIdEscale() != null) {
            Escale escale = new Escale();
            escale.setIdEscale(request.getIdEscale());
            enteteFacture.setEscale(escale);
        }

        return enteteFacture;
    }

    public EnteteFactureResponse toResponse(EnteteFacture enteteFacture) {
        EnteteFactureResponse response = new EnteteFactureResponse();
        response.setIdEnteteFacture(enteteFacture.getIdEnteteFacture());
        response.setNumero(enteteFacture.getNumero());
        response.setDate(enteteFacture.getDate());
        response.setClient(enteteFacture.getClient());
        response.setTotalHT(enteteFacture.getTotalHT());
        response.setTaux_TVA(enteteFacture.getTaux_TVA());
        response.setMontant_TVA(enteteFacture.getMontant_TVA());
        response.setTotalTTC(enteteFacture.getTotalTTC());
        response.setStatut(enteteFacture.getStatut());

        if (enteteFacture.getEscale() != null) {
            response.setIdEscale(enteteFacture.getEscale().getIdEscale());
        }

        if (enteteFacture.getDetailFactures() != null) {
            response.setDetails(enteteFacture.getDetailFactures().stream()
                    .map(detailFactureMapper::toResponse).toList());
        }

        return response;
    }
}
