package org.example.controllers;

import org.example.lua.LuaModManager;
import org.example.models.AppUser;
import org.example.models.dto.UserCreateDto;
import org.example.models.dto.UserPublicDto;
import org.example.services.persistance.UserDbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDbService userDbService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private LuaModManager luaModManager;

    private AppUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new AppUser();
        testUser.setId(1L);
        testUser.setUsername("alice");
        testUser.setEmail("alice@example.com");
        testUser.setPassword("secret");
    }


    @Test
    void createUser_shouldReturnOk_whenUserIsValid() throws Exception {
        UserCreateDto dto = new UserCreateDto("alice", "alice@example.com", "secret");

        // Mock userDbService.createUser
        when(userDbService.createUser(any(AppUser.class))).thenReturn(testUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(luaModManager.getInstance()).thenReturn(luaModManager);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {"username":"alice","email":"alice@example.com","password":"secret"}
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("alice"));

        verify(userDbService).createUser(any(AppUser.class));
        verify(luaModManager).triggerEvent(eq("onUserCreate"), any());
    }

    @Test
    void getUserById_shouldReturnUser_whenExists() throws Exception {
        when(userDbService.getUserById(1L)).thenReturn(Optional.of(testUser));
        when(luaModManager.getInstance()).thenReturn(luaModManager);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"));

        verify(luaModManager).triggerEvent(eq("onGetUserById"), any());
    }

    @Test
    void updateUser_shouldReturnOk_whenCurrentUserMatches() throws Exception {
        when(userDbService.getUserById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userDbService.saveUser(any())).thenReturn(testUser);
        when(luaModManager.getInstance()).thenReturn(luaModManager);

        String json = """
        {"username":"alice2","email":"alice2@example.com","password":"newpass"}
        """;

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .principal(() -> "alice") // optional, for AuthenticationPrincipal
                )
                .andExpect(status().isOk());

        verify(userDbService).saveUser(any());
        verify(luaModManager).triggerEvent(eq("onUpdateUserById"), any());
    }

    @Test
    void deleteUser_shouldReturnNoContent_whenCurrentUserMatches() throws Exception {
        when(luaModManager.getInstance()).thenReturn(luaModManager);

        mockMvc.perform(delete("/users/1")
                        .principal(() -> "alice"))
                .andExpect(status().isNoContent());

        verify(userDbService).deleteUserById(1L);
        verify(luaModManager).triggerEvent(eq("onDeleteUserById"), isNull());
    }
}
