package itis.khabibullina.service;


import itis.khabibullina.dto.CreateUserRequestDto;
import itis.khabibullina.dto.UserDto;
import itis.khabibullina.model.Role;
import itis.khabibullina.model.User;
import itis.khabibullina.repository.UserRepository;
import itis.khabibullina.service.impl.UserServiceImpl;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserService userService;
    @MockBean
    private static UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private EmailServices emailServices;
    private final String name = "Ivan";
    private UserDto userDto;
    private User user;



    @BeforeEach
    public void setBehaviour() {
        userDto = new UserDto(name);

        Role role = new Role(1L, "ADMIN", null);
        user = new User(1L, name, "ivan@mail.ru", "qwerty123", null, true,
                Collections.singleton(role));

        userService = new UserServiceImpl(userRepository, encoder, emailServices);
    }

    @Test
    public void testFindAllByName() {
        given(userRepository.findAllByName(name)).willReturn(Collections.singletonList(user));

        List<UserDto> users = userService.findAllByName(name);
        Assertions.assertEquals(Collections.singletonList(userDto), users);
        verify(userRepository, times(1)).findAllByName(name);

    }

    @Test
    public void testCreateUser() throws TemplateException, IOException {
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName(name);
        createUserRequestDto.setEmail("leyla.khbi@gmail.com");
        createUserRequestDto.setPassword("qwerty123");

        given(userRepository.save(any(User.class))).willReturn(user);

        UserDto receivedUserDto = userService.create(createUserRequestDto);
        Assertions.assertEquals(userDto, receivedUserDto);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testSuccessfulVerification() {
        String code = "Aaa";
        when(userRepository.findByVerificationCode(code)).thenReturn(Optional.of(user));

        boolean answer = userService.verify(code);
        Assertions.assertTrue(answer);
        verify(userRepository, times(1)).findByVerificationCode(code);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testFailedVerification() {
        String code = "Aaa";
        when(userRepository.findByVerificationCode(code)).thenReturn(Optional.empty());

        boolean answer = userService.verify(code);
        Assertions.assertFalse(answer);
        verify(userRepository, times(1)).findByVerificationCode(code);
        verify(userRepository, times(0)).save(any(User.class));
    }
}
