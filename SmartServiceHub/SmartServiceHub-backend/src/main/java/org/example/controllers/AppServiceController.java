package org.example.controllers;

import org.example.models.AppService;
import org.example.models.AppUser;
import org.example.services.persistance.AppServiceDbService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping
    public ResponseEntity<AppService> createService(
            @RequestBody AppService service,
            @AuthenticationPrincipal AppUser currentUser) {

        service.setUser_id(currentUser.getId());

        AppService savedService = serviceDbService.saveService(service);
        return ResponseEntity.ok(savedService);
    }

    @GetMapping
    public ResponseEntity<List<AppService>> getAllServices() {
        List<AppService> services = serviceDbService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppService> getServiceById(@PathVariable("id") Long id) {
        Optional<AppService> serviceOpt = serviceDbService.getServiceById(id);
        return serviceOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/limit/{limit}")
    public ResponseEntity<List<AppService>> getServiceWithLimit(@PathVariable("limit") Integer limit) {
        List<AppService> services = serviceDbService.getServiceWithLimit(limit);
        return ResponseEntity.ok(services);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceById(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        Optional<AppService> serviceOpt = serviceDbService.getServiceById(id);

        if (serviceOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AppService service = serviceOpt.get();

        if (!currentUser.getId().equals(service.getUser_id())) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        serviceDbService.deleteServiceById(id);
        return ResponseEntity.noContent().build();
    }
}
