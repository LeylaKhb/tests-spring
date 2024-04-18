package itis.khabibullina.controller;

import com.google.gson.Gson;
import itis.khabibullina.dto.CreateUserRequestDto;
import itis.khabibullina.dto.UserDto;
import itis.khabibullina.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    private final Gson gson = new Gson();

    @Test
    public void testGetUsers() throws Exception {
        UserDto userDto = new UserDto("Ivan");
        given(userService.findAllByName("Ivan")).willReturn(Arrays.asList(userDto));

        mockMvc.perform(get("/users?name=Ivan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("user").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Ivan"));
    }

    @Test
    public void testCreateUser() throws Exception {

        CreateUserRequestDto user = new CreateUserRequestDto();
        user.setName(null);
        user.setEmail(null);
        user.setPassword(null);

        UserDto userDto = new UserDto("Ivan");
        given(userService.create(user)).willReturn(userDto);

        mockMvc.perform(post("/user")
                        .content(gson.toJson(user))
                        .with(csrf())
                        .with(user("user").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("sign_up_success"))
                .andExpect(content().string(containsString("Success")));

        System.out.println(user);
        verify(userService, times(1)).create(user);
    }

    @Test
    public void testVerificationSuccess() throws Exception {
        String code = "aa";
        given(userService.verify(code)).willReturn(true);

        mockMvc.perform(get("/verification?code=" + code)
                        .with(user("user").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("verification_success"))
                .andExpect(content().string(containsString("Verification success")));

        verify(userService, times(1)).verify(code);
    }

    @Test
    public void testVerificationFailed() throws Exception {
        String code = "aa";
        given(userService.verify(code)).willReturn(false);

        mockMvc.perform(get("/verification?code=" + code)
                        .with(user("user").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("verification_failed"))
                .andExpect(content().string(containsString("Verification failed")));

        verify(userService, times(1)).verify(code);
    }
}
