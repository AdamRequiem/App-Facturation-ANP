package ma.anp.facturation_droits_port.service;

import ma.anp.facturation_droits_port.dto.PortRequest;
import ma.anp.facturation_droits_port.dto.PortResponse;
import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.exception.ResourceNotFoundException;
import ma.anp.facturation_droits_port.mapper.PortMapper;
import ma.anp.facturation_droits_port.repository.PortRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortService {

    private final PortRepository portRepository;
    private final PortMapper portMapper;

    public PortService(PortRepository portRepository, PortMapper portMapper) {
        this.portRepository = portRepository;
        this.portMapper = portMapper;
    }

    public List<PortResponse> getAllPorts() {
        return portRepository.findAll().stream().map(portMapper::toResponse).toList();
    }

    public Port getPortEntityById(Long id){
        return portRepository.findById(id).orElseThrow(() -> new RuntimeException("Port introuvable"));
    }

    public PortResponse getPortById(Long id){
        Port port = getPortEntityById(id);
        return portMapper.toResponse(port);
    }

    public PortResponse createPort(PortRequest request) {
        Port port = portMapper.toEntity(request);
        Port savedPort = portRepository.save(port);
        return portMapper.toResponse(savedPort);
    }

    public PortResponse updatePort(Long id, PortRequest request) {
        Port existant = getPortEntityById(id);

        existant.setNom(request.getNom());
        existant.setTypeTerminal(request.getTypeTerminal());

        Port updatedPort = portRepository.save(existant);
        return portMapper.toResponse(updatedPort);
    }

    public void deletePort (Long id) {
        if(!portRepository.existsById(id)) {
            throw new ResourceNotFoundException("Port introuvable avec l'id : "+id);
        }
        portRepository.deleteById(id);
    }
}
