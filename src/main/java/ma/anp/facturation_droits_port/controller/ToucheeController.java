package ma.anp.facturation_droits_port.controller;

import jakarta.validation.Valid;
import ma.anp.facturation_droits_port.dto.ToucheeRequest;
import ma.anp.facturation_droits_port.dto.ToucheeResponse;
import ma.anp.facturation_droits_port.service.ToucheeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/touchees")
public class ToucheeController {

    private final ToucheeService toucheeService;

    public ToucheeController(ToucheeService toucheeService) {
        this.toucheeService = toucheeService;
    }

    @GetMapping
    public List<ToucheeResponse> getAllTouchees() {
        return toucheeService.getAllTouchees();
    }

    @GetMapping("/{id}")
    public ToucheeResponse getToucheeById(@PathVariable Long id) {
        return toucheeService.getToucheeById(id);
    }

    @PostMapping
    public ResponseEntity<ToucheeResponse> createTouchee(@Valid @RequestBody ToucheeRequest request) {
        ToucheeResponse response = toucheeService.createTouchee(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ToucheeResponse updateTouchee(@PathVariable Long id, @Valid @RequestBody ToucheeRequest request) {
        return toucheeService.updateTouchee(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTouchee(@PathVariable Long id) {
        toucheeService.deleteTouchee(id);
        return ResponseEntity.noContent().build();
    }
}
