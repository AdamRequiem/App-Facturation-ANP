package ma.anp.facturation_droits_port.service;

import ma.anp.facturation_droits_port.dto.MarchandiseRequest;
import ma.anp.facturation_droits_port.dto.MarchandiseResponse;
import ma.anp.facturation_droits_port.entity.Marchandise;
import ma.anp.facturation_droits_port.exception.ResourceNotFoundException;
import ma.anp.facturation_droits_port.mapper.MarchandiseMapper;
import ma.anp.facturation_droits_port.repository.MarchandiseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarchandiseService {

    private final MarchandiseRepository marchandiseRepository;
    private final MarchandiseMapper marchandiseMapper;

    public MarchandiseService(MarchandiseRepository marchandiseRepository, MarchandiseMapper marchandiseMapper) {
        this.marchandiseRepository = marchandiseRepository;
        this.marchandiseMapper = marchandiseMapper;
    }

    public List<MarchandiseResponse> getAllMarchandises() {
        return marchandiseRepository.findAll().stream().map(marchandiseMapper::toResponse).toList();
    }

    public Marchandise getMarchandiseEntityById(Long id) {
        return marchandiseRepository.findById(id).orElseThrow(() -> new RuntimeException("Marchandise introuvable"));
    }

    public MarchandiseResponse getMarchandiseById(Long id) {
        Marchandise marchandise = getMarchandiseEntityById(id);
        return marchandiseMapper.toResponse(marchandise);
    }

    public MarchandiseResponse createMarchandise(MarchandiseRequest request) {
        Marchandise marchandise = marchandiseMapper.toEntity(request);
        Marchandise savedMarchandise = marchandiseRepository.save(marchandise);
        return marchandiseMapper.toResponse(savedMarchandise);
    }

    public MarchandiseResponse updateMarchandise(Long id, MarchandiseRequest request) {
        Marchandise existant = getMarchandiseEntityById(id);

        existant.setNom(request.getNom());

        Marchandise updatedMarchandise = marchandiseRepository.save(existant);
        return marchandiseMapper.toResponse(updatedMarchandise);
    }

    public void deleteMarchandise(Long id) {
        if(!marchandiseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Marchandise introuvable avec l'id : "+id);
        }
        marchandiseRepository.deleteById(id);
    }
}
