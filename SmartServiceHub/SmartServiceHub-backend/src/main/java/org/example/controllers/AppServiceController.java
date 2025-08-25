package org.example.controllers;

import org.example.lua.LuaModManager;
import org.example.lua.LuaTableAdaptor;
import org.example.models.ServicePost;
import org.example.models.AppUser;
import org.example.models.dto.AppServiceCreateDto;
import org.example.models.dto.AppServicePublicDto;
import org.example.services.persistance.AppServiceDbService;
import org.luaj.vm2.LuaTable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServicePost> createService(
            @RequestBody AppServiceCreateDto service,
            @AuthenticationPrincipal AppUser currentUser
    ){
        ServicePost savedService = serviceDbService.saveService(ServicePost.fromCreateDto(service, currentUser));

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onAppServiceCreate", LuaTableAdaptor.fromAppServiceCreateDto(savedService));

        return ResponseEntity.status(HttpStatus.CREATED).body(savedService);
    }

    @GetMapping
    public ResponseEntity<List<ServicePost>> getAllServices() {
        return ResponseEntity.ok(serviceDbService.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicePost> getServiceById(@PathVariable("id") Long id) {
        Optional<ServicePost> maybeAppService = serviceDbService.getServiceById(id);

        if (maybeAppService.isPresent()){
            ServicePost servicePost = maybeAppService.get();

            LuaModManager luaManager = LuaModManager.getInstance();
            LuaTable safeAppService = new LuaTable();

            luaManager.triggerEvent("onGetServiceById", safeAppService);

            return ResponseEntity.ok(servicePost);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/limit/{limit}")
    public ResponseEntity<List<ServicePost>> getServiceWithLimit(@PathVariable("limit") Integer limit) {
        return ResponseEntity.ok(serviceDbService.getServiceWithLimit(limit));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicePost> updateService(
            @PathVariable Long id,
            @RequestBody ServicePost serviceUpdate,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ServicePost existing = serviceDbService.getServiceById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!currentUser.getId().equals(existing.getCreatorId())) {
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

        ServicePost service = serviceDbService.getServiceById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!currentUser.getId().equals(service.getCreatorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        serviceDbService.deleteServiceById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unique")
    public ResponseEntity<List<AppServicePublicDto>> getUniqueServicePost(
            @RequestParam int limit,
            @RequestParam int offset
    ){
        return ResponseEntity.ok(AppServicePublicDto.fromAppServiceList(serviceDbService.getServicePostUnique(limit, (offset * limit))));
    }
}
