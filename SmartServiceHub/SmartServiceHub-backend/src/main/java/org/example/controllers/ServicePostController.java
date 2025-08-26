package org.example.controllers;

import org.example.lua.LuaModManager;
import org.example.lua.LuaTableAdaptor;
import org.example.models.ServicePost;
import org.example.models.AppUser;
import org.example.models.dto.ServicePostCreateDto;
import org.example.models.dto.ServicePostPublicDto;
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
public class ServicePostController {
    // todo: pass the proper tables to lua

    private final AppServiceDbService serviceDbService;

    public ServicePostController(AppServiceDbService serviceDbService) {
        this.serviceDbService = serviceDbService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServicePost> createService(
            @RequestBody ServicePostCreateDto service,
            @AuthenticationPrincipal AppUser currentUser
    ){
        ServicePost savedService = serviceDbService.saveService(ServicePost.fromCreateDto(service, currentUser));

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onAppServiceCreate", LuaTableAdaptor.fromAppServiceCreateDto(savedService));

        return ResponseEntity.status(HttpStatus.CREATED).body(savedService);
    }

    // this one should probably be deleted too
    // nah later
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

        ServicePost updatedPost = serviceDbService.saveService(existing);

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onUpdateService", LuaTableAdaptor.fromServicePost(updatedPost));

        return ResponseEntity.ok(updatedPost);
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

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onDeleteServicePostById", null);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unique")
    public ResponseEntity<List<ServicePostPublicDto>> getUniqueServicePost(
            @RequestParam int limit,
            @RequestParam int offset
    ){

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onGetUniqueServicePost", null);

        return ResponseEntity.ok(ServicePostPublicDto.fromAppServiceList(serviceDbService.getServicePostUnique(limit, (offset * limit))));
    }
}
