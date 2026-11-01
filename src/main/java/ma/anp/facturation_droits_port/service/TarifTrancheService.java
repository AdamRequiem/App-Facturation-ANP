package ma.anp.facturation_droits_port.service;

import ma.anp.facturation_droits_port.dto.TarifTrancheRequest;
import ma.anp.facturation_droits_port.dto.TarifTrancheResponse;
import ma.anp.facturation_droits_port.entity.TarifTranche;
import ma.anp.facturation_droits_port.exception.ResourceNotFoundException;
import ma.anp.facturation_droits_port.mapper.TarifTrancheMapper;
import ma.anp.facturation_droits_port.repository.TarifTrancheRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarifTrancheService {

    private final TarifTrancheRepository tarifTrancheRepository;
    private final TarifTrancheMapper tarifTrancheMapper;

    public TarifTrancheService(TarifTrancheRepository tarifTrancheRepository, TarifTrancheMapper tarifTrancheMapper) {
        this.tarifTrancheRepository = tarifTrancheRepository;
        this.tarifTrancheMapper = tarifTrancheMapper;
    }

    public List<TarifTrancheResponse> getAllTarifTranches() {
        return tarifTrancheRepository.findAll().stream().map(tarifTrancheMapper::toResponse).toList();
    }

    public TarifTranche getTarifTrancheEntityById(Long id) {
        return tarifTrancheRepository.findById(id).orElseThrow(() -> new RuntimeException("TarifTranche introuvable"));
    }

    public TarifTrancheResponse getTarifTrancheById(Long id) {
        TarifTranche tarifTranche = getTarifTrancheEntityById(id);
        return tarifTrancheMapper.toResponse(tarifTranche);
    }

    public TarifTrancheResponse createTarifTranche(TarifTrancheRequest request) {
        TarifTranche tarifTranche = tarifTrancheMapper.toEntity(request);
        TarifTranche savedTarifTranche = tarifTrancheRepository.save(tarifTranche);
        return tarifTrancheMapper.toResponse(savedTarifTranche);
    }

    public TarifTrancheResponse updateTarifTranche(Long id, TarifTrancheRequest request) {
        TarifTranche existant = getTarifTrancheEntityById(id);

        existant.setValeurMin(request.getValeurMin());
        existant.setValeurMax(request.getValeurMax());
        existant.setMontant(request.getMontant());
        existant.setUnite(request.getUnite());

        TarifTranche updatedTarifTranche = tarifTrancheRepository.save(existant);
        return tarifTrancheMapper.toResponse(updatedTarifTranche);
    }

    public void deleteTarifTranche(Long id) {
        if (!tarifTrancheRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tarif Tranche introuable avec l'id : " + id);
        }
        tarifTrancheRepository.deleteById(id);
    }
}
