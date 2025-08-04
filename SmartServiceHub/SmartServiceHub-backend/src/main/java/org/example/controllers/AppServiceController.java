package org.example.controllers;

import org.example.models.AppService;
import org.example.services.persistance.AppServiceDbService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/services")
public class AppServiceController {

    private final AppServiceDbService serviceDbService;

    public AppServiceController(AppServiceDbService serviceDbService) {
        this.serviceDbService = serviceDbService;
    }

    // Create or update a service
    @PostMapping
    public ResponseEntity<AppService> createService(@RequestBody AppService service) {
        AppService savedService = serviceDbService.saveService(service);
        return ResponseEntity.ok(savedService);
    }

    // Get all services
    @GetMapping
    public ResponseEntity<List<AppService>> getAllServices() {
        List<AppService> services = serviceDbService.getAllServices();
        return ResponseEntity.ok(services);
    }

    // Get a service by id
    @GetMapping("/{id}")
    public ResponseEntity<AppService> getServiceById(@PathVariable("id") Long id) {
        Optional<AppService> serviceOpt = serviceDbService.getServiceById(id);
        return serviceOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{limit}")
    public ResponseEntity<List<AppService>> getServiceWithLimit(@PathVariable("limit") int limit) {
        List<AppService> services = serviceDbService.getServiceWithLimit(limit);
        return ResponseEntity.ok(services);
    }

    // Delete a service by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceById(@PathVariable Long id) {
        serviceDbService.deleteServiceById(id);
        return ResponseEntity.noContent().build();
    }
}
