package ma.anp.facturation_droits_port.service;

import ma.anp.facturation_droits_port.dto.EscaleRequest;
import ma.anp.facturation_droits_port.dto.EscaleResponse;
import ma.anp.facturation_droits_port.entity.EnteteFacture;
import ma.anp.facturation_droits_port.entity.Escale;
import ma.anp.facturation_droits_port.entity.Navire;
import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.exception.ResourceNotFoundException;
import ma.anp.facturation_droits_port.mapper.EscaleMapper;
import ma.anp.facturation_droits_port.repository.EscaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EscaleService {

    private final EscaleRepository escaleRepository;
    private final EscaleMapper escaleMapper;
    private final ToucheeService toucheeService;

    public EscaleService(EscaleRepository escaleRepository, EscaleMapper escaleMapper, ToucheeService toucheeService) {
        this.escaleRepository = escaleRepository;
        this.escaleMapper = escaleMapper;
        this.toucheeService = toucheeService;
    }

    public List<EscaleResponse> getAllEscales() {
        return escaleRepository.findAll().stream().map(escaleMapper::toResponse).toList();
    }

    public Escale getEscaleEntityById(long id) {
        return escaleRepository.findById(id).orElseThrow(() -> new RuntimeException("Escale introuvable"));
    }

    public EscaleResponse getEscaleById(long id) {
        Escale escale = getEscaleEntityById(id);
        return escaleMapper.toResponse(escale);
    }

    public EscaleResponse createEscale(EscaleRequest request) {
        Escale escale = escaleMapper.toEntity(request);
        Escale savedEscale = escaleRepository.save(escale);
        toucheeService.incrementer(savedEscale.getNavire(), savedEscale.getPort(), savedEscale.getDateEntree().getYear());
        return escaleMapper.toResponse(savedEscale);
    }

    public EscaleResponse updateEscale(long id, EscaleRequest request) {
        Escale existant = getEscaleEntityById(id);

        existant.setStatut(request.getStatut());
        existant.setProvenance(request.getProvenance());
        existant.setDestination(request.getDestination());
        existant.setDateEntree(request.getDateEntree());
        existant.setDateSortie(request.getDateSortie());

        if (request.getIdNavire() != null) {
            Navire navire = new Navire();
            navire.setIdNavire(request.getIdNavire());
            existant.setNavire(navire);
        }

        if (request.getIdPort() != null) {
            Port port = new Port();
            port.setIdPort(request.getIdPort());
            existant.setPort(port);
        }

        if (request.getIdEnteteFacture() != null) {
            EnteteFacture enteteFacture = new EnteteFacture();
            enteteFacture.setIdEnteteFacture(request.getIdEnteteFacture());
            existant.setEnteteFacture(enteteFacture);
        }

        Escale updatedEscale = escaleRepository.save(existant);
        return escaleMapper.toResponse(updatedEscale);
    }

    public void deleteEscale(Long id) {
        if (!escaleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Escale introuvable avec l'id : " + id);
        }
        escaleRepository.deleteById(id);
    }
}
