package org.example.controllers;

import org.example.models.AppService;
import org.example.models.AppUser;
import org.example.services.persistance.AppServiceDbService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/services")
public class AppServiceController {

    private final AppServiceDbService serviceDbService;

    public AppServiceController(AppServiceDbService serviceDbService) {
        this.serviceDbService = serviceDbService;
    }

    @PostMapping
    public ResponseEntity<AppService> createService(
            @RequestBody AppService service
            ,@AuthenticationPrincipal AppUser currentUser
    ){
        System.out.println("creating a new service");

        if (currentUser == null) {

            System.out.println("Failed auth");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        System.out.println("auth success");

        service.setUserId(currentUser.getId());
        AppService savedService = serviceDbService.saveService(service);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedService);
    }

    @GetMapping
    public ResponseEntity<List<AppService>> getAllServices() {
        return ResponseEntity.ok(serviceDbService.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppService> getServiceById(@PathVariable("id") Long id) {
        return serviceDbService.getServiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/limit/{limit}")
    public ResponseEntity<List<AppService>> getServiceWithLimit(@PathVariable("limit") Integer limit) {
        return ResponseEntity.ok(serviceDbService.getServiceWithLimit(limit));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppService> updateService(
            @PathVariable Long id,
            @RequestBody AppService serviceUpdate,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AppService existing = serviceDbService.getServiceById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!currentUser.getId().equals(existing.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        existing.setTitle(serviceUpdate.getTitle());
        existing.setContent(serviceUpdate.getContent());

        return ResponseEntity.ok(serviceDbService.saveService(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceById(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AppService service = serviceDbService.getServiceById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!currentUser.getId().equals(service.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        serviceDbService.deleteServiceById(id);
        return ResponseEntity.noContent().build();
    }
}
