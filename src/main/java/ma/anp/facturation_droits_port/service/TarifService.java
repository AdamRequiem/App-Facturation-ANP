package ma.anp.facturation_droits_port.service;

import ma.anp.facturation_droits_port.dto.TarifRequest;
import ma.anp.facturation_droits_port.dto.TarifResponse;
import ma.anp.facturation_droits_port.entity.Marchandise;
import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.entity.Tarif;
import ma.anp.facturation_droits_port.exception.ResourceNotFoundException;
import ma.anp.facturation_droits_port.mapper.TarifMapper;
import ma.anp.facturation_droits_port.repository.TarifRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarifService {

    private final TarifRepository tarifRepository;
    private final TarifMapper tarifMapper;

    public TarifService(TarifRepository tarifRepository, TarifMapper tarifMapper) {
        this.tarifRepository = tarifRepository;
        this.tarifMapper = tarifMapper;
    }

    public List<TarifResponse> getAllTarifs() {
        return tarifRepository.findAll().stream().map(tarifMapper::toResponse).toList();
    }

    public Tarif getTarifEntityById(Long id) {
        return tarifRepository.findById(id).orElseThrow(() -> new RuntimeException("Tarif introuvable"));
    }

    public TarifResponse getTarifById(Long id) {
        Tarif tarif = getTarifEntityById(id);
        return tarifMapper.toResponse(tarif);
    }

    public TarifResponse createTarif(TarifRequest request) {
        Tarif tarif = tarifMapper.toEntity(request);
        Tarif savedTarif = tarifRepository.save(tarif);
        return tarifMapper.toResponse(savedTarif);
    }

    public TarifResponse updateTarif(Long id, TarifRequest request) {
        Tarif existant = getTarifEntityById(id);

        existant.setDescription(request.getDescription());
        existant.setTypeOperation(request.getTypeOperation());
        existant.setTypeNavire(request.getTypeNavire());
        existant.setUnite(request.getUnite());

        if (request.getIdMarchandise() != null) {
            Marchandise marchandise = new Marchandise();
            marchandise.setIdMarchandise(request.getIdMarchandise());
            existant.setMarchandise(marchandise);
        }

        if (request.getIdPort() != null) {
            Port port = new Port();
            port.setIdPort(request.getIdPort());
            existant.setPort(port);
        }

        Tarif updatedTarif = tarifRepository.save(existant);
        return tarifMapper.toResponse(updatedTarif);
    }

    public void deleteTarif(Long id) {
        if (!tarifRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tarif introuvable avec l'id : " + id);
        }
        tarifRepository.deleteById(id);
    }
}
