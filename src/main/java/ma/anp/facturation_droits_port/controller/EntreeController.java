package ma.anp.facturation_droits_port.controller;

import ma.anp.facturation_droits_port.dto.EntreeRequest;
import ma.anp.facturation_droits_port.dto.EntreeResponse;
import ma.anp.facturation_droits_port.service.EntreeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entrees")
public class EntreeController {

    private final EntreeService entreeService;

    public EntreeController(EntreeService entreeService) {
        this.entreeService = entreeService;
    }

    @GetMapping
    public List<EntreeResponse> getAllEntrees() {
        return entreeService.getAllEntrees();
    }

    @GetMapping("/{id}")
    public EntreeResponse getEntreeById(@PathVariable Long id) {
        return entreeService.getEntreeById(id);
    }

    @PostMapping
    public ResponseEntity<EntreeResponse> createEntree(@RequestBody EntreeRequest request) {
        EntreeResponse response = entreeService.createEntree(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public EntreeResponse updateEntree(@PathVariable Long id, @RequestBody EntreeRequest request) {
        return entreeService.updateEntree(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntree(@PathVariable Long id) {
        entreeService.deleteEntree(id);

        return ResponseEntity.noContent().build();
    }
}
