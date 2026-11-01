package ma.anp.facturation_droits_port.service;

import ma.anp.facturation_droits_port.dto.StationnementRequest;
import ma.anp.facturation_droits_port.dto.StationnementResponse;
import ma.anp.facturation_droits_port.entity.Escale;
import ma.anp.facturation_droits_port.entity.Marchandise;
import ma.anp.facturation_droits_port.entity.Navire;
import ma.anp.facturation_droits_port.entity.Stationnement;
import ma.anp.facturation_droits_port.exception.ResourceNotFoundException;
import ma.anp.facturation_droits_port.mapper.StationnementMapper;
import ma.anp.facturation_droits_port.repository.StationnementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationnementService {

    private final StationnementRepository stationnementRepository;
    private final StationnementMapper stationnementMapper;

    public StationnementService(StationnementRepository stationnementRepository,
            StationnementMapper stationnementMapper) {
        this.stationnementRepository = stationnementRepository;
        this.stationnementMapper = stationnementMapper;
    }

    public List<StationnementResponse> getAllStationnements() {
        return stationnementRepository.findAll().stream().map(stationnementMapper::toResponse).toList();
    }

    public Stationnement getStationnementEntityById(Long id) {
        return stationnementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stationnement introuvable"));
    }

    public StationnementResponse getStationnementById(Long id) {
        Stationnement stationnement = getStationnementEntityById(id);
        return stationnementMapper.toResponse(stationnement);
    }

    public StationnementResponse createStationnement(StationnementRequest request) {
        Stationnement stationnement = stationnementMapper.toEntity(request);
        Stationnement savedStationnement = stationnementRepository.save(stationnement);
        return stationnementMapper.toResponse(savedStationnement);
    }

    public StationnementResponse updateStationnement(Long id, StationnementRequest request) {
        Stationnement existant = getStationnementEntityById(id);

        existant.setEmbarquement(request.isEmbarquement());
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

        Stationnement updatedStationnement = stationnementRepository.save(existant);
        return stationnementMapper.toResponse(updatedStationnement);
    }

    public void deleteStationnement(Long id) {
        if (!stationnementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Stationnement introuvable avec l'id : " + id);
        }
        stationnementRepository.deleteById(id);
    }
}
