package org.example.controllers;

import org.example.lua.LuaModManager;
import org.example.lua.LuaTableAdaptor;
import org.example.models.ServicePost;
import org.example.models.AppUser;
import org.example.models.dto.ServicePostCreateDto;
import org.example.models.dto.ServicePostPublicDto;
import org.example.models.responses_requests.GenericErrorResponse;
import org.example.models.responses_requests.NotFoundResponse;
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
    private final LuaModManager luaManager;

    public ServicePostController(AppServiceDbService serviceDbService, LuaModManager luaManager) {
        this.serviceDbService = serviceDbService;
        this.luaManager = luaManager;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> createService(
            @RequestBody ServicePostCreateDto service,
            @AuthenticationPrincipal AppUser currentUser
    ){
        if (service.getTitle() == null || service.getContent() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericErrorResponse("Error, malformed request! Parameters are as follows: title, content"));
        }
        ServicePost savedService = serviceDbService.saveService(ServicePost.fromCreateDto(service, currentUser));

        luaManager.triggerEvent("onAppServiceCreate", LuaTableAdaptor.fromAppServiceCreateDto(savedService));

        return ResponseEntity.status(HttpStatus.CREATED).body(savedService);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getServiceById(@PathVariable("id") Long id) {
        Optional<ServicePost> maybeAppService = serviceDbService.getServiceById(id);

        if (maybeAppService.isPresent()){
            ServicePost servicePost = maybeAppService.get();

            LuaTable safeAppService = new LuaTable();
            luaManager.triggerEvent("onGetServiceById", safeAppService);

            return ResponseEntity.ok(servicePost);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Service with id %d not found!".formatted(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateService(
            @PathVariable Long id,
            @RequestBody ServicePost serviceUpdate,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponse("User not logged in!"));
        }

        ServicePost existing = serviceDbService.getServiceById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!currentUser.getId().equals(existing.getCreatorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericErrorResponse("This user cannot edit this post because it did not create it or does not have enough permissions!"));
        }

        existing.setTitle(serviceUpdate.getTitle());
        existing.setContent(serviceUpdate.getContent());

        ServicePost updatedPost = serviceDbService.saveService(existing);

        luaManager.triggerEvent("onUpdateService", LuaTableAdaptor.fromServicePost(updatedPost));

        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteServiceById(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponse("User not logged in!"));
        }

        ServicePost service = serviceDbService.getServiceById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!currentUser.getId().equals(service.getCreatorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericErrorResponse("This user cannot edit this post because it did not create it or does not have enough permissions!"));
        }

        serviceDbService.deleteServiceById(id);

        luaManager.triggerEvent("onDeleteServicePostById", null);

        return ResponseEntity.status(HttpStatus.OK).body(new GenericErrorResponse("Service %d successfully deleted!"));
    }

    @GetMapping("/unique")
    public ResponseEntity<List<ServicePostPublicDto>> getUniqueServicePost(
            @RequestParam int limit,
            @RequestParam int offset
    ){

        luaManager.triggerEvent("onGetUniqueServicePost", null);

        return ResponseEntity.ok(ServicePostPublicDto.fromAppServiceList(serviceDbService.getServicePostUnique(limit, (offset * limit))));
    }
}
