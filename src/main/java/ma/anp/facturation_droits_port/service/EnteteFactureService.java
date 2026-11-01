package ma.anp.facturation_droits_port.service;

import ma.anp.facturation_droits_port.dto.EnteteFactureRequest;
import ma.anp.facturation_droits_port.dto.EnteteFactureResponse;
import ma.anp.facturation_droits_port.entity.EnteteFacture;
import ma.anp.facturation_droits_port.exception.ResourceNotFoundException;
import ma.anp.facturation_droits_port.facturation.StatutFacture;
import ma.anp.facturation_droits_port.mapper.EnteteFactureMapper;
import ma.anp.facturation_droits_port.repository.EnteteFactureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnteteFactureService {

    private final EnteteFactureRepository enteteFactureRepository;
    private  final EnteteFactureMapper enteteFactureMapper;

    public EnteteFactureService(EnteteFactureRepository enteteFactureRepository, EnteteFactureMapper enteteFactureMapper) {
        this.enteteFactureRepository = enteteFactureRepository;
        this.enteteFactureMapper = enteteFactureMapper;
    }

    @Transactional(readOnly = true)
    public List<EnteteFactureResponse> getAllEnteteFactures() {
        return enteteFactureRepository.findAll().stream().map(enteteFactureMapper::toResponse).toList();
    }

    public EnteteFacture getEnteteFactureEntityById(Long id) {
        return enteteFactureRepository.findById(id).orElseThrow(()->new RuntimeException("Entete Facture introuvable"));
    }

    @Transactional
    public EnteteFactureResponse getEnteteFactureById(Long id) {
        EnteteFacture enteteFacture = getEnteteFactureEntityById(id);
        return enteteFactureMapper.toResponse(enteteFacture);
    }

    public EnteteFactureResponse createEnteteFacture(EnteteFactureRequest request) {

        EnteteFacture enteteFacture = enteteFactureMapper.toEntity(request);
        EnteteFacture savedEnteteFacture = enteteFactureRepository.save(enteteFacture);
        return enteteFactureMapper.toResponse(savedEnteteFacture);
    }

    public EnteteFactureResponse updateEnteteFacture(Long id, EnteteFactureRequest request) {
        EnteteFacture existant = getEnteteFactureEntityById(id);

        existant.setNumero(request.getNumero());
        existant.setClient(request.getClient());
        existant.setDate(request.getDate());
        existant.setTotalHT(request.getTotalHT());
        existant.setTaux_TVA(request.getTaux_TVA());
        existant.setMontant_TVA(request.getMontant_TVA());
        existant.setTotalTTC(request.getTotalTTC());
        existant.setStatut(request.getStatut());

        EnteteFacture updatedEnteteFacture = enteteFactureRepository.save(existant);
        return enteteFactureMapper.toResponse(updatedEnteteFacture);
    }

    public void deleteEnteteFacture(Long id) {
        if (!enteteFactureRepository.existsById(id)) {
            throw new ResourceNotFoundException("Entete Facture introuvable avec l'ID : "+id);
        }
        enteteFactureRepository.deleteById(id);
    }

    @Transactional
    public EnteteFactureResponse validerFacture(Long id) {
        EnteteFacture enteteFacture = getEnteteFactureEntityById(id);

        if (!StatutFacture.BROUILLON.equals(enteteFacture.getStatut())) {
            throw new IllegalStateException(
                "Seule une facture au statut BROUILLON peut être validée (statut actuel : " + enteteFacture.getStatut() + ")");
        }

        enteteFacture.setStatut(StatutFacture.VALIDEE);
        EnteteFacture validee = enteteFactureRepository.save(enteteFacture);
        return enteteFactureMapper.toResponse(validee);
    }
}
