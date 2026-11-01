package ma.anp.facturation_droits_port.service;

import ma.anp.facturation_droits_port.dto.NavireRequest;
import ma.anp.facturation_droits_port.dto.NavireResponse;
import ma.anp.facturation_droits_port.entity.Navire;
import ma.anp.facturation_droits_port.mapper.NavireMapper;
import ma.anp.facturation_droits_port.repository.NavireRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavireService {

    private final NavireRepository navireRepository;
    private final NavireMapper navireMapper;

    public NavireService(NavireRepository navireRepository, NavireMapper navireMapper) {
        this.navireRepository = navireRepository;
        this.navireMapper = navireMapper;
    }

    public List<NavireResponse> getAllNavires() {
        return navireRepository.findAll()
                .stream()
                .map(navireMapper::toResponse)
                .toList();
    }

    public NavireResponse getNavireById(Long id) {
        Navire navire = findNavireEntityById(id);
        return navireMapper.toResponse(navire);
    }

    public NavireResponse createNavire(NavireRequest request) {
        Navire navire = navireMapper.toEntity(request);
        Navire savedNavire = navireRepository.save(navire);
        return navireMapper.toResponse(savedNavire);
    }

    public NavireResponse updateNavire(Long id, NavireRequest request) {
        Navire existant = findNavireEntityById(id);

        existant.setNom(request.getNom());
        existant.setTjb(request.getTjb());
        existant.setTypeNavire(request.getTypeNavire());
        existant.setLongueur(request.getLongueur());
        existant.setLargeur(request.getLargeur());
        existant.setTirantEau(request.getTirantEau());
        existant.setArmateur(request.getArmateur());

        Navire updatedNavire = navireRepository.save(existant);
        return navireMapper.toResponse(updatedNavire);
    }

    public void deleteNavire(Long id) {
        if (!navireRepository.existsById(id)) {
            throw new RuntimeException("Navire introuvable avec l'ID : " + id);
        }
        navireRepository.deleteById(id);
    }

    private Navire findNavireEntityById(Long id) {
        return navireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Navire introuvable avec l'ID : " + id));
    }
}
