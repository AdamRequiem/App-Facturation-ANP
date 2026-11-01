package ma.anp.facturation_droits_port.mapper;

import ma.anp.facturation_droits_port.dto.StationnementRequest;
import ma.anp.facturation_droits_port.dto.StationnementResponse;
import ma.anp.facturation_droits_port.entity.Escale;
import ma.anp.facturation_droits_port.entity.Marchandise;
import ma.anp.facturation_droits_port.entity.Navire;
import ma.anp.facturation_droits_port.entity.Stationnement;
import org.springframework.stereotype.Component;

@Component
public class StationnementMapper {

    public Stationnement toEntity(StationnementRequest request) {
        Stationnement stationnement = new Stationnement();
        stationnement.setEmbarquement(request.isEmbarquement());
        stationnement.setAbonnement(request.isAbonnement());
        stationnement.setExoneration(request.isExoneration());
        stationnement.setTypeRistourne(request.getTypeRistourne());

        if (request.getIdNavire() != null) {
            Navire navire = new Navire();
            navire.setIdNavire(request.getIdNavire());
            stationnement.setNavire(navire);
        }

        if (request.getIdMarchandise() != null) {
            Marchandise marchandise = new Marchandise();
            marchandise.setIdMarchandise(request.getIdMarchandise());
            stationnement.setMarchandise(marchandise);
        }

        if (request.getIdEscale() != null) {
            Escale escale = new Escale();
            escale.setIdEscale(request.getIdEscale());
            stationnement.setEscale(escale);
        }

        return stationnement;
    }

    public StationnementResponse toResponse(Stationnement stationnement) {
        StationnementResponse response = new StationnementResponse();
        response.setIdStationnement(stationnement.getIdStationnement());
        response.setEmbarquement(stationnement.isEmbarquement());
        response.setAbonnement(stationnement.isAbonnement());
        response.setExoneration(stationnement.isExoneration());
        response.setTypeRistourne(stationnement.getTypeRistourne());

        if (stationnement.getNavire() != null) {
            response.setIdNavire(stationnement.getNavire().getIdNavire());
            response.setNomNavire(stationnement.getNavire().getNom());
        }

        if (stationnement.getMarchandise() != null) {
            response.setIdMarchandise(stationnement.getMarchandise().getIdMarchandise());
            response.setNomMarchandise(stationnement.getMarchandise().getNom());
        }

        if (stationnement.getEscale() != null) {
            response.setIdEscale(stationnement.getEscale().getIdEscale());
        }

        return response;
    }
}
