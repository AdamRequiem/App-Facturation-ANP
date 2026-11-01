package ma.anp.facturation_droits_port.controller;

import jakarta.validation.Valid;
import ma.anp.facturation_droits_port.dto.NavireRequest;
import ma.anp.facturation_droits_port.dto.NavireResponse;
import ma.anp.facturation_droits_port.service.NavireService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/navires")
public class NavireController {

    private final NavireService navireService;

    public NavireController(NavireService navireService) {
        this.navireService = navireService;
    }

    @GetMapping
    public List<NavireResponse> getAllNavires() {
        return navireService.getAllNavires();
    }

    @GetMapping("/{id}")
    public NavireResponse getNavireById(@PathVariable Long id) {
        return navireService.getNavireById(id);
    }

    @PostMapping
    public ResponseEntity<NavireResponse> createNavire(@Valid @RequestBody NavireRequest request) {

        NavireResponse response = navireService.createNavire(request);

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public NavireResponse updateNavire(
            @PathVariable Long id,
            @Valid @RequestBody NavireRequest request) {

        return navireService.updateNavire(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNavire(@PathVariable Long id) {

        navireService.deleteNavire(id);

        return ResponseEntity.noContent().build();
    }
}
