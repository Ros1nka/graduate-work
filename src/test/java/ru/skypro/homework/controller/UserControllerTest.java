package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.service.UserService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "test@example.com")
    void setPassword_ShouldReturnOk() throws Exception {
        NewPassword newPassword = new NewPassword("oldPassword", "newPassword");

        when(userService.changePassword(eq("user@example.com"), eq("oldPassword"), eq("newPassword")))
                .thenReturn(true);

        String requestJson = "{" +
                "\"currentPassword\":\"oldPassword\"," +
                "\"newPassword\":\"newPassword\"" +
                "}";

        mockMvc.perform(post("/users/set_password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void setPassword_WhenWrongPassword_ShouldReturn403() throws Exception {
        when(userService.changePassword(anyString(), anyString(), anyString()))
                .thenReturn(false);

        mockMvc.perform(post("/users/set_password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currentPassword\":\"wrong12345\",\"newPassword\":\"new123456\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void setPassword_WhenUnauthorized_ShouldReturn401() throws Exception {
        mockMvc.perform(post("/users/set_password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getUser_ShouldReturnUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("Test");

        when(userService.getUser(eq("test@example.com"))).thenReturn(userDTO);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Test"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("NewName");
        updateUser.setLastName("NewLastName");
        updateUser.setPhone("+712345678");

        when(userService.updateUser(eq("test@example.com"), any(UpdateUser.class)))
                .thenReturn(updateUser);

        String requestJson = "{" +
                "\"firstName\":\"NewName\"," +
                "\"lastName\":\"NewLastName\"," +
                "\"phone\":\"+712345678\"" +
                "}";

        mockMvc.perform(patch("/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("NewName"))
                .andExpect(jsonPath("$.lastName").value("NewLastName"))
                .andExpect(jsonPath("$.phone").value("+712345678"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void updateUserImage_ShouldReturnOk() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PATCH, "/users/me/image")
                        .file(imageFile)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void updateUserImage_WhenUnauthorized_ShouldReturn401() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image".getBytes()
        );

        mockMvc.perform(multipart("/users/me/image")
                        .file(imageFile)
                        .with(csrf())
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isUnauthorized());
    }
}