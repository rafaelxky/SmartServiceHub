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
    public ResponseEntity<Object> createService(
            @RequestBody ServicePostCreateDto service,
            @AuthenticationPrincipal AppUser currentUser
    ){
        if (service.getTitle() == null || service.getContent() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("""
                    {
                        "error": "Error, malformed request! Parameters are as follows: title, content"
                    }
                    """);
        }
        ServicePost savedService = serviceDbService.saveService(ServicePost.fromCreateDto(service, currentUser));

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onAppServiceCreate", LuaTableAdaptor.fromAppServiceCreateDto(savedService));

        return ResponseEntity.status(HttpStatus.CREATED).body(savedService);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getServiceById(@PathVariable("id") Long id) {
        Optional<ServicePost> maybeAppService = serviceDbService.getServiceById(id);

        if (maybeAppService.isPresent()){
            ServicePost servicePost = maybeAppService.get();

            LuaModManager luaManager = LuaModManager.getInstance();
            LuaTable safeAppService = new LuaTable();
            luaManager.triggerEvent("onGetServiceById", safeAppService);

            return ResponseEntity.ok(servicePost);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("""
                {
                    "not_found": "Service with id %d not found!"
                }
                """.formatted(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateService(
            @PathVariable Long id,
            @RequestBody ServicePost serviceUpdate,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("""
                    {
                        "error": "User not logged in!"
                    }
                    """);
        }

        ServicePost existing = serviceDbService.getServiceById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!currentUser.getId().equals(existing.getCreatorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("""
                    {
                        "error": "User id and post creator id mismatch!"
                    }
                    """);
        }

        existing.setTitle(serviceUpdate.getTitle());
        existing.setContent(serviceUpdate.getContent());

        ServicePost updatedPost = serviceDbService.saveService(existing);

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onUpdateService", LuaTableAdaptor.fromServicePost(updatedPost));

        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteServiceById(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("""
                    {
                        "error": "User not logged in!"
                    }
                    """);
        }

        ServicePost service = serviceDbService.getServiceById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!currentUser.getId().equals(service.getCreatorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("""
                    {
                        "error": "User id and post creator id mismatch!"
                    }
                    """);

        }

        serviceDbService.deleteServiceById(id);

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onDeleteServicePostById", null);

        return ResponseEntity.status(HttpStatus.OK).body("""
                {
                    "message": "Service %d successfully deleted!"
                }
                """.formatted(id));
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
