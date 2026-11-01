package ma.anp.facturation_droits_port.service;

import ma.anp.facturation_droits_port.dto.MouillageRequest;
import ma.anp.facturation_droits_port.dto.MouillageResponse;
import ma.anp.facturation_droits_port.entity.Escale;
import ma.anp.facturation_droits_port.entity.Marchandise;
import ma.anp.facturation_droits_port.entity.Mouillage;
import ma.anp.facturation_droits_port.entity.Navire;
import ma.anp.facturation_droits_port.exception.ResourceNotFoundException;
import ma.anp.facturation_droits_port.mapper.MouillageMapper;
import ma.anp.facturation_droits_port.repository.MouillageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MouillageService {

    private final MouillageRepository mouillageRepository;
    private final MouillageMapper mouillageMapper;

    public MouillageService(MouillageRepository mouillageRepository, MouillageMapper mouillageMapper) {
        this.mouillageRepository = mouillageRepository;
        this.mouillageMapper = mouillageMapper;
    }

    public List<MouillageResponse> getAllMouillages() {
        return mouillageRepository.findAll().stream().map(mouillageMapper::toResponse).toList();
    }

    public Mouillage getMouillageEntityById(Long id) {
        return mouillageRepository.findById(id).orElseThrow(() -> new RuntimeException("Mouillage introuvable"));
    }

    public MouillageResponse getMouillageById(Long id) {
        Mouillage mouillage = getMouillageEntityById(id);
        return mouillageMapper.toResponse(mouillage);
    }

    public MouillageResponse createMouillage(MouillageRequest request) {
        Mouillage mouillage = mouillageMapper.toEntity(request);
        Mouillage savedMouillage = mouillageRepository.save(mouillage);
        return mouillageMapper.toResponse(savedMouillage);
    }

    public MouillageResponse updateMouillage(Long id, MouillageRequest request) {
        Mouillage existant = getMouillageEntityById(id);

        existant.setDateAccostage(request.getDateAccostage());
        existant.setAbonnement(request.isAbonnement());
        existant.setExoneration(request.isExoneration());
        existant.setTypeRistourne(request.getTypeRistourne());

        if (request.getIdNavire() != null) {
            Navire navire = new Navire();
            navire.setIdNavire(request.getIdNavire());
            existant.setNavire(navire);
        }

        if (request.getIdMarchandise() != null) {
            Marchandise marchandise = new Marchandise();
            marchandise.setIdMarchandise(request.getIdMarchandise());
            existant.setMarchandise(marchandise);
        }

        if (request.getIdEscale() != null) {
            Escale escale = new Escale();
            escale.setIdEscale(request.getIdEscale());
            existant.setEscale(escale);
        }

        Mouillage updatedMouillage = mouillageRepository.save(existant);
        return mouillageMapper.toResponse(updatedMouillage);
    }

    public void deleteMouillage(Long id) {
        if (!mouillageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mouillage introuvable avec l'id : " + id);
        }
        mouillageRepository.deleteById(id);
    }
}
