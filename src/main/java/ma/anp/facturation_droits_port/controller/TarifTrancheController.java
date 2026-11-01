package ma.anp.facturation_droits_port.controller;

import ma.anp.facturation_droits_port.dto.TarifTrancheRequest;
import ma.anp.facturation_droits_port.dto.TarifTrancheResponse;
import ma.anp.facturation_droits_port.service.TarifTrancheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarif-tranches")
public class TarifTrancheController {

    private final TarifTrancheService tarifTrancheService;

    public TarifTrancheController(TarifTrancheService tarifTrancheService) {
        this.tarifTrancheService = tarifTrancheService;
    }

    @GetMapping
    public List<TarifTrancheResponse> getAllTarifTranche() {
        return tarifTrancheService.getAllTarifTranches();
    }

    @GetMapping("/{id}")
    public TarifTrancheResponse getTarifTrancheById(@PathVariable Long id) {
        return tarifTrancheService.getTarifTrancheById(id);
    }

    @PostMapping
    public ResponseEntity<TarifTrancheResponse> createTarifTranche(@RequestBody TarifTrancheRequest request) {
        TarifTrancheResponse response = tarifTrancheService.createTarifTranche(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public TarifTrancheResponse updateTarifTranche(@PathVariable Long id, @RequestBody TarifTrancheRequest request) {
        return tarifTrancheService.updateTarifTranche(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarifTranche(@PathVariable Long id) {
        tarifTrancheService.deleteTarifTranche(id);
        return ResponseEntity.noContent().build();
    }
}
