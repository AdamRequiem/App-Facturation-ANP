package ma.anp.facturation_droits_port.controller;

import ma.anp.facturation_droits_port.dto.MouillageRequest;
import ma.anp.facturation_droits_port.dto.MouillageResponse;
import ma.anp.facturation_droits_port.service.MouillageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mouillages")
public class MouillageController {

    private final MouillageService mouillageService;

    public MouillageController(MouillageService mouillageService) {
        this.mouillageService = mouillageService;
    }

    @GetMapping
    public List<MouillageResponse> getAllMouillage() {
        return mouillageService.getAllMouillages();
    }

    @GetMapping("/{id}")
    public MouillageResponse getMouillageById(@PathVariable Long id) {
        return mouillageService.getMouillageById(id);
    }

    @PostMapping
    public ResponseEntity<MouillageResponse> createMouillage(@RequestBody MouillageRequest request) {
        MouillageResponse response = mouillageService.createMouillage(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public MouillageResponse updateMouillage(@PathVariable Long id, @RequestBody MouillageRequest request) {
        return mouillageService.updateMouillage(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMouillage(@PathVariable Long id) {
        mouillageService.deleteMouillage(id);
        return ResponseEntity.noContent().build();
    }
}
