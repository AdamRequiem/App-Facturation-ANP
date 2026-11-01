package ma.anp.facturation_droits_port.mapper;

import ma.anp.facturation_droits_port.dto.EscaleRequest;
import ma.anp.facturation_droits_port.dto.EscaleResponse;
import ma.anp.facturation_droits_port.entity.EnteteFacture;
import ma.anp.facturation_droits_port.entity.Escale;
import ma.anp.facturation_droits_port.entity.Navire;
import ma.anp.facturation_droits_port.entity.Port;
import org.springframework.stereotype.Component;

@Component
public class EscaleMapper {

    public Escale toEntity(EscaleRequest request) {
        Escale escale = new Escale();
        escale.setProvenance(request.getProvenance());
        escale.setDestination(request.getDestination());
        escale.setDateEntree(request.getDateEntree());
        escale.setDateSortie(request.getDateSortie());
        escale.setStatut(request.getStatut());

        if (request.getIdNavire() != null) {
            Navire navire = new Navire();
            navire.setIdNavire(request.getIdNavire());
            escale.setNavire(navire);
        }

        if (request.getIdPort() != null) {
            Port port = new Port();
            port.setIdPort(request.getIdPort());
            escale.setPort(port);
        }

        if (request.getIdEnteteFacture() != null) {
            EnteteFacture enteteFacture = new EnteteFacture();
            enteteFacture.setIdEnteteFacture(request.getIdEnteteFacture());
            escale.setEnteteFacture(enteteFacture);
        }

        return escale;
    }

    public EscaleResponse toResponse(Escale escale) {
        EscaleResponse response = new EscaleResponse();
        response.setIdEscale(escale.getIdEscale());
        response.setProvenance(escale.getProvenance());
        response.setDestination(escale.getDestination());
        response.setDateEntree(escale.getDateEntree());
        response.setDateSortie(escale.getDateSortie());
        response.setStatut(escale.getStatut());

        if (escale.getNavire() != null) {
            response.setIdNavire(escale.getNavire().getIdNavire());
            response.setNomNavire(escale.getNavire().getNom());
        }

        if (escale.getPort() != null) {
            response.setIdPort(escale.getPort().getIdPort());
            response.setNomPort(escale.getPort().getNom());
        }

        if (escale.getEnteteFacture() != null) {
            response.setIdEnteteFacture(escale.getEnteteFacture().getIdEnteteFacture());
            response.setNumeroEnteteFacture(escale.getEnteteFacture().getNumero());
        }

        return response;
    }
}
