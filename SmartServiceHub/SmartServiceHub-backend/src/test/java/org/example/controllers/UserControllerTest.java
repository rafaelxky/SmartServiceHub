package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.TestEnvSetup;
import org.example.config.DataInitializer;
import org.example.lua.LuaModManager;
import org.example.models.AppUser;
import org.example.models.Roles;
import org.example.models.dto.UserCreateDto;
import org.example.services.persistance.UserDbService;
import org.example.services.persistance.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@ContextConfiguration(initializers = TestEnvSetup.class)
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

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void config(){
        new TestEnvSetup();
    }

    @Test
    public void successGetUserById() throws Exception {
        AppUser user1 = new AppUser(1L, "name", "mail", "encoded-pass", Roles.USER.roleName, LocalDateTime.now());

        when(userDbService.getUserById(1L)).thenReturn(Optional.of(user1));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("name"))
                .andExpect(jsonPath("$.email").value("mail"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.role").doesNotExist());
    }

    @Test
    public void userNotFound() throws Exception {
        mockMvc.perform(get("/users/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void successCreateUser() throws Exception {
        AppUser user1 = new AppUser(1L, "name", "mail", "pass", Roles.USER.roleName, LocalDateTime.now());

        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> "encoded-" + invocation.getArgument(0));

        when(userDbService.createUser(any(AppUser.class)))
                .thenReturn(user1);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content("""
                                {
                                    "username": "name",
                                    "email": "mail",
                                    "password": "pass"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("name"))
                .andExpect(jsonPath("$.email").value("mail"))
                .andExpect(jsonPath("$.id").value(1))
        ;
    }


    @Test
    public void noBodyCreateUser() throws Exception {
        AppUser user1 = new AppUser(1L, "name", "mail", "pass", Roles.USER.roleName, LocalDateTime.now());

        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> "encoded-" + invocation.getArgument(0));

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content("""
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void successUserUpdate() throws Exception {
        AppUser currentUser = new AppUser(1L, "name", "mail", "pass", Roles.USER.roleName, LocalDateTime.now());

        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> "encoded-" + invocation.getArgument(0));

        when(userDbService.getUserById(1L)).thenReturn(Optional.of(currentUser));
        when(userDbService.saveUser(any(AppUser.class))).thenReturn(currentUser);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "name",
                                    "email": "mail",
                                    "password": "pass"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("name"))
                .andExpect(jsonPath("$.email").value("mail"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void noLoginUserUpdate() throws Exception {
        AppUser currentUser = new AppUser(1L, "name", "mail", "pass", Roles.USER.roleName, LocalDateTime.now());

        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> "encoded-" + invocation.getArgument(0));

        when(userDbService.getUserById(1L)).thenReturn(Optional.of(currentUser));
        when(userDbService.saveUser(any(AppUser.class))).thenReturn(currentUser);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "name",
                                    "email": "mail",
                                    "password": "pass"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void wrongUserUpdate() throws Exception {
        AppUser currentUser = new AppUser(1L, "name", "mail", "pass", Roles.USER.roleName, LocalDateTime.now());
        AppUser wrongUser = new AppUser(2L, "name2", "mail2", "pass2", Roles.USER.roleName, LocalDateTime.now());

        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> "encoded-" + invocation.getArgument(0));

        when(userDbService.getUserById(1L)).thenReturn(Optional.of(currentUser));
        when(userDbService.saveUser(any(AppUser.class))).thenReturn(currentUser);

        UserCreateDto updateDto = new UserCreateDto("name", "mail", "pass");

        // wrong user logged in (forbidden)
        UsernamePasswordAuthenticationToken authFail =
                new UsernamePasswordAuthenticationToken(wrongUser, null, wrongUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authFail);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "name",
                                    "email": "mail",
                                    "password": "pass"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    public void noBodyUserUpdate() throws Exception {
        AppUser currentUser = new AppUser(1L, "name", "mail", "pass", Roles.USER.roleName, LocalDateTime.now());

        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> "encoded-" + invocation.getArgument(0));

        when(userDbService.getUserById(1L)).thenReturn(Optional.of(currentUser));
        when(userDbService.saveUser(any(AppUser.class))).thenReturn(currentUser);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void noAuthDeleteUser() throws Exception {
        AppUser currentUser = new AppUser(1L, "name", "mail", "pass", Roles.USER.roleName, LocalDateTime.now());

        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> "encoded-" + invocation.getArgument(0));

        when(userDbService.getUserById(1L)).thenReturn(Optional.of(currentUser));

        mockMvc.perform(delete("/users/1")).andExpect(status().isUnauthorized());
    }

    @Test
    public void wrongUserDeleteUser() throws Exception {
        AppUser currentUser = new AppUser(1L, "name", "mail", "pass", Roles.USER.roleName, LocalDateTime.now());
        AppUser wrongUser = new AppUser(2L, "name2", "mail2", "pass2", Roles.USER.roleName, LocalDateTime.now());

        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> "encoded-" + invocation.getArgument(0));

        when(userDbService.getUserById(1L)).thenReturn(Optional.of(currentUser));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(wrongUser, null, wrongUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(delete("/users/1")).andExpect(status().isForbidden());
    }

    @Test
    public void successDeleteUser() throws Exception {
        AppUser currentUser = new AppUser(1L, "name", "mail", "pass", Roles.USER.roleName, LocalDateTime.now());

        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> "encoded-" + invocation.getArgument(0));

        when(userDbService.getUserById(1L)).thenReturn(Optional.of(currentUser));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(delete("/users/1")).andExpect(status().isOk());
    }

    @Test
    public void noSuchUserDeleteUser() throws Exception {
        AppUser currentUser = new AppUser(1L, "name", "mail", "pass", Roles.USER.roleName, LocalDateTime.now());

        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> "encoded-" + invocation.getArgument(0));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(delete("/users/2")).andExpect(status().isForbidden());
    }
    
    @Test
    public void successGetUniqueUsers() throws Exception{
        AppUser currentUser3 = new AppUser(3L, "name3", "mail3", "pass3", Roles.USER.roleName, LocalDateTime.now());
        AppUser currentUser4 = new AppUser(4L, "name4", "mail4", "pass4", Roles.USER.roleName, LocalDateTime.now());

        AppUser[] userList = {currentUser3, currentUser4};
        when(userDbService.getUserUnique(2, 2)).thenReturn(Arrays.asList(userList));

        mockMvc.perform(get("/users/unique?limit=2&offset=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("name3"))
                .andExpect(jsonPath("$[0].email").value("mail3"))
                .andExpect(jsonPath("$[1].username").value("name4"))
                .andExpect(jsonPath("$[1].email").value("mail4"));
    }

    @Test
    public void emptyGetUniqueUser() throws Exception{
        mockMvc.perform(get("/users/unique?limit=-1&offset=-1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void badRequestGetUniqueUser() throws Exception{
        mockMvc.perform(get("/users/unique?limit=hello&offset=world"))
                .andExpect(status().isBadRequest());
    }
}