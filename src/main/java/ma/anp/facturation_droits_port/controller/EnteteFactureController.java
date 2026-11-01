package ma.anp.facturation_droits_port.controller;

import ma.anp.facturation_droits_port.dto.EnteteFactureRequest;
import ma.anp.facturation_droits_port.dto.EnteteFactureResponse;
import ma.anp.facturation_droits_port.service.EnteteFactureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entete-factures")
public class EnteteFactureController {

    private final EnteteFactureService enteteFactureService;

    public EnteteFactureController(EnteteFactureService enteteFactureService) {
        this.enteteFactureService = enteteFactureService;
    }

    @GetMapping
    public List<EnteteFactureResponse> getAllEnteteFactures() {
        return enteteFactureService.getAllEnteteFactures();
    }

    @GetMapping("/{id}")
    public EnteteFactureResponse getEnteteFactureById(@PathVariable Long id) {
        return enteteFactureService.getEnteteFactureById(id);
    }

    @PostMapping
    public ResponseEntity<EnteteFactureResponse> createEnteteFacture(@RequestBody EnteteFactureRequest request) {
        EnteteFactureResponse response = enteteFactureService.createEnteteFacture(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public EnteteFactureResponse updateEnteteFacture(@PathVariable Long id, @RequestBody EnteteFactureRequest request) {
        return enteteFactureService.updateEnteteFacture(id, request);
    }

    @PutMapping("/{id}/valider")
    public EnteteFactureResponse validerFacture(@PathVariable Long id) {
        return enteteFactureService.validerFacture(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnteteFacture(@PathVariable Long id) {
        enteteFactureService.deleteEnteteFacture(id);
        return ResponseEntity.noContent().build();
    }
}
