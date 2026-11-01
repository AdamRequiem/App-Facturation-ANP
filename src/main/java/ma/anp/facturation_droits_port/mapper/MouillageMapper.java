package ma.anp.facturation_droits_port.mapper;

import ma.anp.facturation_droits_port.dto.MouillageRequest;
import ma.anp.facturation_droits_port.dto.MouillageResponse;
import ma.anp.facturation_droits_port.entity.Escale;
import ma.anp.facturation_droits_port.entity.Marchandise;
import ma.anp.facturation_droits_port.entity.Mouillage;
import ma.anp.facturation_droits_port.entity.Navire;
import org.springframework.stereotype.Component;

@Component
public class MouillageMapper {

    public Mouillage toEntity(MouillageRequest request) {
        Mouillage mouillage = new Mouillage();
        mouillage.setDateAccostage(request.getDateAccostage());
        mouillage.setAbonnement(request.isAbonnement());
        mouillage.setExoneration(request.isExoneration());
        mouillage.setTypeRistourne(request.getTypeRistourne());

        if (request.getIdNavire() != null) {
            Navire navire = new Navire();
            navire.setIdNavire(request.getIdNavire());
            mouillage.setNavire(navire);
        }

        if (request.getIdMarchandise() != null) {
            Marchandise marchandise = new Marchandise();
            marchandise.setIdMarchandise(request.getIdMarchandise());
            mouillage.setMarchandise(marchandise);
        }

        if (request.getIdEscale() != null) {
            Escale escale = new Escale();
            escale.setIdEscale(request.getIdEscale());
            mouillage.setEscale(escale);
        }

        return mouillage;
    }

    public MouillageResponse toResponse(Mouillage mouillage) {
        MouillageResponse response = new MouillageResponse();
        response.setIdMouillage(mouillage.getIdMouillage());
        response.setDateAccostage(mouillage.getDateAccostage());
        response.setAbonnement(mouillage.isAbonnement());
        response.setExoneration(mouillage.isExoneration());
        response.setTypeRistourne(mouillage.getTypeRistourne());

        if (mouillage.getNavire() != null) {
            response.setIdNavire(mouillage.getNavire().getIdNavire());
            response.setNomNavire(mouillage.getNavire().getNom());
        }

        if (mouillage.getMarchandise() != null) {
            response.setIdMarchandise(mouillage.getMarchandise().getIdMarchandise());
            response.setNomMarchandise(mouillage.getMarchandise().getNom());
        }

        if (mouillage.getEscale() != null) {
            response.setIdEscale(mouillage.getEscale().getIdEscale());
        }

        return response;
    }
}
