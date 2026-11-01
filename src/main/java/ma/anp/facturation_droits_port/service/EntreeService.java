package ma.anp.facturation_droits_port.service;

import ma.anp.facturation_droits_port.dto.EntreeRequest;
import ma.anp.facturation_droits_port.dto.EntreeResponse;
import ma.anp.facturation_droits_port.entity.Escale;
import ma.anp.facturation_droits_port.entity.Entree;
import ma.anp.facturation_droits_port.entity.Navire;
import ma.anp.facturation_droits_port.exception.ResourceNotFoundException;
import ma.anp.facturation_droits_port.mapper.EntreeMapper;
import ma.anp.facturation_droits_port.repository.EntreeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntreeService {

    private final EntreeRepository entreeRepository;
    private final EntreeMapper entreeMapper;

    public EntreeService(EntreeRepository entreeRepository, EntreeMapper entreeMapper) {
        this.entreeRepository = entreeRepository;
        this.entreeMapper = entreeMapper;
    }

    public List<EntreeResponse> getAllEntrees() {
        return entreeRepository.findAll().stream().map(entreeMapper::toResponse).toList();
    }

    public Entree getEntreeEntityById(Long id) {
        return entreeRepository.findById(id).orElseThrow(() -> new RuntimeException("Entree introuvable"));
    }

    public EntreeResponse getEntreeById(Long id) {
        Entree entree = getEntreeEntityById(id);
        return entreeMapper.toResponse(entree);
    }

    public EntreeResponse createEntree(EntreeRequest request) {
        Entree entree = entreeMapper.toEntity(request);
        Entree savedEntree = entreeRepository.save(entree);
        return entreeMapper.toResponse(savedEntree);
    }

    public EntreeResponse updateEntree(Long id, EntreeRequest request) {
        Entree existant = getEntreeEntityById(id);

        existant.setRaison(request.getRaison());
        existant.setFacturationNext(request.isFacturationNext());
        existant.setExoneration(request.isExoneration());
        existant.setTypeRistourne(request.getTypeRistourne());

        if (request.getIdNavire() != null) {
            Navire navire = new Navire();
            navire.setIdNavire(request.getIdNavire());
            existant.setNavire(navire);
        }

        if (request.getIdEscale() != null) {
            Escale escale = new Escale();
            escale.setIdEscale(request.getIdEscale());
            existant.setEscale(escale);
        }

        Entree updatedEntree = entreeRepository.save(existant);
        return entreeMapper.toResponse(updatedEntree);
    }

    public void deleteEntree(Long id) {
        if (!entreeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Entrée introuvable avec l'id : " + id);
        }
        entreeRepository.deleteById(id);
    }
}
