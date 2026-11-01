package ma.anp.facturation_droits_port.controller;

import ma.anp.facturation_droits_port.dto.PortRequest;
import ma.anp.facturation_droits_port.dto.PortResponse;
import ma.anp.facturation_droits_port.service.PortService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ports")
public class PortController {

    private final PortService portService;

    public PortController(PortService portService) {
        this.portService = portService;
    }

    @GetMapping
    public List<PortResponse> getAllPorts() {
        return portService.getAllPorts();
    }

    @GetMapping("/{id}")
    public PortResponse getPortById(@PathVariable Long id) {
        return portService.getPortById(id);
    }

    @PostMapping
    public ResponseEntity<PortResponse> createPort(@RequestBody PortRequest request) {
        PortResponse response = portService.createPort(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping
    public PortResponse updatePort(@PathVariable Long id, @RequestBody PortRequest request) {
        return portService.updatePort(id, request);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePort(@PathVariable Long id) {
        portService.deletePort(id);
        return ResponseEntity.noContent().build();
    }
}
