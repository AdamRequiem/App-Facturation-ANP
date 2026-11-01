package ma.anp.facturation_droits_port.service;

import ma.anp.facturation_droits_port.dto.DetailFactureRequest;
import ma.anp.facturation_droits_port.dto.DetailFactureResponse;
import ma.anp.facturation_droits_port.entity.DetailFacture;
import ma.anp.facturation_droits_port.entity.EnteteFacture;
import ma.anp.facturation_droits_port.entity.Entree;
import ma.anp.facturation_droits_port.entity.Mouillage;
import ma.anp.facturation_droits_port.entity.Stationnement;
import ma.anp.facturation_droits_port.entity.Tarif;
import ma.anp.facturation_droits_port.exception.ResourceNotFoundException;
import ma.anp.facturation_droits_port.mapper.DetailFactureMapper;
import ma.anp.facturation_droits_port.repository.DetailFactureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetailFactureService {

    private final DetailFactureRepository detailFactureRepository;
    private final DetailFactureMapper detailFactureMapper;

    public DetailFactureService(DetailFactureRepository detailFactureRepository,
            DetailFactureMapper detailFactureMapper) {
        this.detailFactureRepository = detailFactureRepository;
        this.detailFactureMapper = detailFactureMapper;
    }

    public List<DetailFactureResponse> getAllDetailFactures() {
        return detailFactureRepository.findAll().stream().map(detailFactureMapper::toResponse).toList();
    }

    public DetailFacture getDetailFactureEntityById(Long id) {
        return detailFactureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DetailFacture introuvable"));
    }

    public DetailFactureResponse getDetailFactureById(Long id) {
        DetailFacture detailFacture = getDetailFactureEntityById(id);
        return detailFactureMapper.toResponse(detailFacture);
    }

    public DetailFactureResponse createDetailFacture(DetailFactureRequest request) {
        DetailFacture detailFacture = detailFactureMapper.toEntity(request);
        DetailFacture savedDetailFacture = detailFactureRepository.save(detailFacture);
        return detailFactureMapper.toResponse(savedDetailFacture);
    }

    public DetailFactureResponse updateDetailFacture(Long id, DetailFactureRequest request) {
        DetailFacture existant = getDetailFactureEntityById(id);

        existant.setDescription(request.getDescription());
        existant.setTypeDroit(request.getTypeDroit());
        existant.setQuantiteHeure(request.getQuantiteHeure());
        existant.setTauxHoraire(request.getTauxHoraire());
        existant.setUnite(request.getUnite());
        existant.setMontantHT(request.getMontantHT());

        if (request.getIdEntree() != null) {
            Entree entree = new Entree();
            entree.setIdEntree(request.getIdEntree());
            existant.setEntree(entree);
        }

        if (request.getIdMouillage() != null) {
            Mouillage mouillage = new Mouillage();
            mouillage.setIdMouillage(request.getIdMouillage());
            existant.setMouillage(mouillage);
        }

        if (request.getIdStationnement() != null) {
            Stationnement stationnement = new Stationnement();
            stationnement.setIdStationnement(request.getIdStationnement());
            existant.setStationnement(stationnement);
        }

        if (request.getIdEnteteFacture() != null) {
            EnteteFacture enteteFacture = new EnteteFacture();
            enteteFacture.setIdEnteteFacture(request.getIdEnteteFacture());
            existant.setEnteteFacture(enteteFacture);
        }

        if (request.getIdTarif() != null) {
            Tarif tarif = new Tarif();
            tarif.setIdTarif(request.getIdTarif());
            existant.setTarif(tarif);
        }

        DetailFacture updatedDetailFacture = detailFactureRepository.save(existant);
        return detailFactureMapper.toResponse(updatedDetailFacture);
    }

    public void deleteDetailFacture(Long id) {
        if (!detailFactureRepository.existsById(id)) {
            throw new ResourceNotFoundException(("Detail Facture introuvable avec l'id : " + id));
        }
        detailFactureRepository.deleteById(id);
    }
}
