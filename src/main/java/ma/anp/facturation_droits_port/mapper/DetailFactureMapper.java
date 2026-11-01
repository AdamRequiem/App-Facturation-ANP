package ma.anp.facturation_droits_port.mapper;

import ma.anp.facturation_droits_port.dto.DetailFactureRequest;
import ma.anp.facturation_droits_port.dto.DetailFactureResponse;
import ma.anp.facturation_droits_port.entity.DetailFacture;
import ma.anp.facturation_droits_port.entity.EnteteFacture;
import ma.anp.facturation_droits_port.entity.Entree;
import ma.anp.facturation_droits_port.entity.Mouillage;
import ma.anp.facturation_droits_port.entity.Stationnement;
import ma.anp.facturation_droits_port.entity.Tarif;
import org.springframework.stereotype.Component;

@Component
public class DetailFactureMapper {

    public DetailFacture toEntity(DetailFactureRequest request) {
        DetailFacture detailFacture = new DetailFacture();
        detailFacture.setTypeDroit(request.getTypeDroit());
        detailFacture.setDescription(request.getDescription());
        detailFacture.setQuantiteHeure(request.getQuantiteHeure());
        detailFacture.setTauxHoraire(request.getTauxHoraire());
        detailFacture.setUnite(request.getUnite());
        detailFacture.setMontantHT(request.getMontantHT());

        if (request.getIdEntree() != null) {
            Entree entree = new Entree();
            entree.setIdEntree(request.getIdEntree());
            detailFacture.setEntree(entree);
        }

        if (request.getIdMouillage() != null) {
            Mouillage mouillage = new Mouillage();
            mouillage.setIdMouillage(request.getIdMouillage());
            detailFacture.setMouillage(mouillage);
        }

        if (request.getIdStationnement() != null) {
            Stationnement stationnement = new Stationnement();
            stationnement.setIdStationnement(request.getIdStationnement());
            detailFacture.setStationnement(stationnement);
        }

        if (request.getIdEnteteFacture() != null) {
            EnteteFacture enteteFacture = new EnteteFacture();
            enteteFacture.setIdEnteteFacture(request.getIdEnteteFacture());
            detailFacture.setEnteteFacture(enteteFacture);
        }

        if (request.getIdTarif() != null) {
            Tarif tarif = new Tarif();
            tarif.setIdTarif(request.getIdTarif());
            detailFacture.setTarif(tarif);
        }

        return detailFacture;
    }

    public DetailFactureResponse toResponse(DetailFacture detailFacture) {
        DetailFactureResponse response = new DetailFactureResponse();
        response.setIdDetailFacture(detailFacture.getIdDetailFacture());
        response.setTypeDroit(detailFacture.getTypeDroit());
        response.setDescription(detailFacture.getDescription());
        response.setQuantiteHeure(detailFacture.getQuantiteHeure());
        response.setTauxHoraire(detailFacture.getTauxHoraire());
        response.setUnite(detailFacture.getUnite());
        response.setMontantHT(detailFacture.getMontantHT());

        if (detailFacture.getEntree() != null) {
            response.setIdEntree(detailFacture.getEntree().getIdEntree());
        }

        if (detailFacture.getMouillage() != null) {
            response.setIdMouillage(detailFacture.getMouillage().getIdMouillage());
        }

        if (detailFacture.getStationnement() != null) {
            response.setIdStationnement(detailFacture.getStationnement().getIdStationnement());
        }

        if (detailFacture.getEnteteFacture() != null) {
            response.setIdEnteteFacture(detailFacture.getEnteteFacture().getIdEnteteFacture());
        }

        if (detailFacture.getTarif() != null) {
            response.setIdTarif(detailFacture.getTarif().getIdTarif());
        }

        return response;
    }
}
