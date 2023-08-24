package ru.practicum.shareit.userTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.user.service.UserMapper;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    private IUserService userService;
    @MockBean
    private UserMapper userMapper;
    @Autowired
    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getByIdTest() throws Exception {
        User user = createUserForTest(1L);

        when(userService.getById(1L)).thenReturn(user);

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName()), String.class))
                .andExpect(jsonPath("$.email", is(user.getEmail()), String.class));

        verify(userService, times(1)).getById(1L);
    }

    @Test
    public void getAllTest() throws Exception {
        when(userService.getAll()).thenReturn(Collections.emptyList());

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(userService, times(1)).getAll();
    }

    @Test
    public void createTest() throws Exception {
        User user = createUserForTest(1L);

        when(userService.create(any(User.class))).thenReturn(user);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName()), String.class))
                .andExpect(jsonPath("$.email", is(user.getEmail()), String.class));

        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    public void updateTest() throws Exception {
        User user = createUserForTest(1L);
        user.setName("updatedName");

        when(userService.update(any(User.class))).thenReturn(user);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName()), String.class))
                .andExpect(jsonPath("$.email", is(user.getEmail()), String.class));

        verify(userService, times(1)).update(any(User.class));
    }

    @Test
    public void deleteTest() throws Exception {
        mvc.perform(delete("/users/1")).andExpect(status().isOk());

        verify(userService, times(1)).deleteById(any(Long.class));
    }

    private User createUserForTest(Long id) {
        String name = "user";
        String email = "user@user.user";

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
