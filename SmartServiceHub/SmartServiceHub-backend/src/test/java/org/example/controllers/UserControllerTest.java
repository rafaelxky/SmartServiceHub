package org.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.example.config.DataInitializer;
import org.example.lua.LuaModManager;
import org.example.lua.LuaStartup;
import org.example.lua.LuaTableAdaptor;
import org.example.models.AppUser;
import org.example.models.Roles;
import org.example.models.dto.UserCreateDto;
import org.example.services.persistance.UserDbService;
import org.example.services.persistance.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDbService userDbService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private DataInitializer dataInitializer;

    @MockBean
    private LuaModManager luaModManager;
    @MockBean
    private LuaTableAdaptor luaTableAdaptor;
    @MockBean
    private LuaStartup luaStartup;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetUserById() throws Exception {
        // Id is always valid because it's handled by db
        // Role and timestamp are also auto managed
        AppUser user1 = new AppUser(1L, "name", "mail", "encoded-pass", Roles.USER.roleName, LocalDateTime.now());

        when(userDbService.getUserById(1L)).thenReturn(Optional.of(user1));

        val resultActions = mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("name"))
                .andExpect(jsonPath("$.email").value("mail"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.role").doesNotExist());

        mockMvc.perform(get("/users/2"))
                .andExpect(status().isNotFound());
    }

@Test
public void testCreateUser() throws Exception {
        // test: normal, malformed / missing parameter
    UserCreateDto user1creation = new UserCreateDto("name", "mail", "pass");
    AppUser user1 = new AppUser(1L, "name", "mail", "pass", Roles.USER.roleName, LocalDateTime.now());

    when(passwordEncoder.encode(anyString()))
            .thenAnswer(invocation -> "encoded-" + invocation.getArgument(0));

    // trying to create any user will return user1
    when(userDbService.createUser(any(AppUser.class)))
            .thenReturn(user1);

    mockMvc.perform(post("/users")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(user1creation)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.username").value("name"))
            .andExpect(jsonPath("$.data.email").value("mail"))
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.password").value("encoded-pass"))
    ;
}

    @Test
    public void testUpdateUser() throws Exception {
        // Mock current user with id 1
        AppUser currentUser = new AppUser(1L, "name", "mail", "pass", Roles.USER.roleName, LocalDateTime.now());
        AppUser wrongUser = new AppUser(2L, "name2", "mail2", "pass2", Roles.USER.roleName, LocalDateTime.now());


        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> "encoded-" + invocation.getArgument(0));

        when(userDbService.getUserById(1L)).thenReturn(Optional.of(currentUser));
        when(userDbService.saveUser(any(AppUser.class))).thenReturn(currentUser);

        UserCreateDto updateDto = new UserCreateDto("name", "mail", "pass");

        // null authentication (unauthorized)
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isUnauthorized());

        // wrong user logged in (forbidden)
        UsernamePasswordAuthenticationToken authFail =
                new UsernamePasswordAuthenticationToken(wrongUser, null, wrongUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authFail);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isForbidden());

        // success
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("name"))
                .andExpect(jsonPath("$.email").value("mail"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testDeleteUser() throws Exception {
        AppUser currentUser = new AppUser(1L, "name", "mail", "pass", Roles.USER.roleName, LocalDateTime.now());
        AppUser wrongUser = new AppUser(2L, "name2", "mail2", "pass2", Roles.USER.roleName, LocalDateTime.now());

        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> "encoded-" + invocation.getArgument(0));

        when(userDbService.getUserById(1L)).thenReturn(Optional.of(currentUser));

        // unauthorized
        mockMvc.perform(delete("/users/1")).andExpect(status().isUnauthorized());

        UsernamePasswordAuthenticationToken auth2 =
                new UsernamePasswordAuthenticationToken(wrongUser, null, wrongUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth2);

        // wrong user
        mockMvc.perform(delete("/users/1")).andExpect(status().isForbidden());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(currentUser, null,currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // no content (success)
        mockMvc.perform(delete("/users/1")).andExpect(status().isNoContent());
    }
}