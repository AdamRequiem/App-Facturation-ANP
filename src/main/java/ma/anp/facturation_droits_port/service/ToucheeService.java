package ma.anp.facturation_droits_port.service;

import jakarta.transaction.Transactional;
import ma.anp.facturation_droits_port.dto.ToucheeRequest;
import ma.anp.facturation_droits_port.dto.ToucheeResponse;
import ma.anp.facturation_droits_port.entity.Navire;
import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.entity.Touchee;
import ma.anp.facturation_droits_port.exception.ResourceNotFoundException;
import ma.anp.facturation_droits_port.mapper.ToucheeMapper;
import ma.anp.facturation_droits_port.repository.ToucheeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToucheeService {

    private final ToucheeRepository toucheeRepository;
    private final ToucheeMapper toucheeMapper;

    public ToucheeService(ToucheeRepository toucheeRepository, ToucheeMapper toucheeMapper) {
        this.toucheeRepository = toucheeRepository;
        this.toucheeMapper = toucheeMapper;
    }

    public List<ToucheeResponse> getAllTouchees() {
        return toucheeRepository.findAll().stream().map(toucheeMapper::toResponse).toList();
    }

    public Touchee getToucheeEntityById(Long id) {
        return toucheeRepository.findById(id).orElseThrow(() -> new RuntimeException("Touchee introuvable"));
    }

    public ToucheeResponse getToucheeById(Long id) {
        Touchee touchee = getToucheeEntityById(id);
        return toucheeMapper.toResponse(touchee);
    }

    public ToucheeResponse createTouchee(ToucheeRequest request) {
        Touchee touchee = toucheeMapper.toEntity(request);
        Touchee saved = toucheeRepository.save(touchee);
        return toucheeMapper.toResponse(saved);
    }

    public ToucheeResponse updateTouchee(Long id, ToucheeRequest request) {
        Touchee existant = getToucheeEntityById(id);

        existant.setAnnee(request.getAnnee());
        existant.setNombreTouchees(request.getNombreTouchees());

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

        Touchee updated = toucheeRepository.save(existant);
        return toucheeMapper.toResponse(updated);
    }

    public void deleteTouchee(Long id) {
        if (!toucheeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Touchée introuvable avec l'id : " + id);
        }
        toucheeRepository.deleteById(id);
    }

    @Transactional
    public int incrementer(Navire navire, Port port, int annee) {
        Touchee touchee = toucheeRepository
                .findByNavire_IdNavireAndPort_IdPortAndAnnee(navire.getIdNavire(), port.getIdPort(), annee)
                .orElseGet(() -> {
                    Touchee nouvelle = new Touchee();
                    nouvelle.setNavire(navire);
                    nouvelle.setPort(port);
                    nouvelle.setAnnee(annee);
                    nouvelle.setNombreTouchees(0);
                    return nouvelle;
                });
        touchee.setNombreTouchees(touchee.getNombreTouchees() + 1);
        return toucheeRepository.save(touchee).getNombreTouchees();
    }

    public int nombreTouchees(Navire navire, Port port, int annee) {
        return toucheeRepository
                .findByNavire_IdNavireAndPort_IdPortAndAnnee(navire.getIdNavire(), port.getIdPort(), annee)
                .map(Touchee::getNombreTouchees)
                .orElse(0);
    }
}
