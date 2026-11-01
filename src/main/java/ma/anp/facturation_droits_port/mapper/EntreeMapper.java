package ma.anp.facturation_droits_port.mapper;

import ma.anp.facturation_droits_port.dto.EntreeRequest;
import ma.anp.facturation_droits_port.dto.EntreeResponse;
import ma.anp.facturation_droits_port.entity.Escale;
import ma.anp.facturation_droits_port.entity.Entree;
import ma.anp.facturation_droits_port.entity.Navire;
import org.springframework.stereotype.Component;

@Component
public class EntreeMapper {

    public Entree toEntity(EntreeRequest request) {
        Entree entree = new Entree();
        entree.setRaison(request.getRaison());
        entree.setFacturationNext(request.isFacturationNext());
        entree.setExoneration(request.isExoneration());
        entree.setTypeRistourne(request.getTypeRistourne());

        if (request.getIdNavire() != null) {
            Navire navire = new Navire();
            navire.setIdNavire(request.getIdNavire());
            entree.setNavire(navire);
        }

        if (request.getIdEscale() != null) {
            Escale escale = new Escale();
            escale.setIdEscale(request.getIdEscale());
            entree.setEscale(escale);
        }

        return entree;
    }

    public EntreeResponse toResponse(Entree entree) {
        EntreeResponse response = new EntreeResponse();
        response.setIdEntree(entree.getIdEntree());
        response.setRaison(entree.getRaison());
        response.setFacturationNext(entree.isFacturationNext());
        response.setExoneration(entree.isExoneration());
        response.setTypeRistourne(entree.getTypeRistourne());

        if (entree.getNavire() != null) {
            response.setIdNavire(entree.getNavire().getIdNavire());
            response.setNomNavire(entree.getNavire().getNom());
        }

        if (entree.getEscale() != null) {
            response.setIdEscale(entree.getEscale().getIdEscale());
        }

        return response;
    }
}
