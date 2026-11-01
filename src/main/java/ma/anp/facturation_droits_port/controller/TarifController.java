package ma.anp.facturation_droits_port.controller;

import ma.anp.facturation_droits_port.dto.TarifRequest;
import ma.anp.facturation_droits_port.dto.TarifResponse;
import ma.anp.facturation_droits_port.service.TarifService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarifs")
public class TarifController {

    private final TarifService tarifService;

    public TarifController(TarifService tarifService) {
        this.tarifService = tarifService;
    }

    @GetMapping
    public List<TarifResponse> getAllTarifs() {
        return tarifService.getAllTarifs();
    }

    @GetMapping("/{id}")
    public TarifResponse getTarifById(@PathVariable Long id) {
        return tarifService.getTarifById(id);
    }

    @PostMapping
    public ResponseEntity<TarifResponse> createTarif(@RequestBody TarifRequest request) {
        TarifResponse response = tarifService.createTarif(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public TarifResponse updateTarif(@PathVariable Long id, @RequestBody TarifRequest request) {
        return tarifService.updateTarif(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarif(@PathVariable Long id) {
        tarifService.deleteTarif(id);
        return ResponseEntity.noContent().build();
    }
}
