package itis.khabibullina.security;

import itis.khabibullina.model.User;
import itis.khabibullina.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomUserDetailsServiceTest {
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEach() {
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    public void testLoadUserByUsernameSuccessfully() {
        String name = "Ivan";
        User user = new User();
        user.setUsername(name);
        given(userRepository.findByUsername(name)).willReturn(Optional.of(user));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(name);
        Assertions.assertEquals(name, userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsernameFailed() {
        String name = "Ivan";
        User user = new User();
        user.setUsername(name);
        given(userRepository.findByUsername(name)).willReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(name);
        });
    }
}
