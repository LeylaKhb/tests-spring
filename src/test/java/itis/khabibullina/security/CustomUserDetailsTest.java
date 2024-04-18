package itis.khabibullina.security;

import itis.khabibullina.model.Role;
import itis.khabibullina.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Collections;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomUserDetailsTest {

    private CustomUserDetails customUserDetails;
    private Role role;
    private final String username = "ivan@mail.ru";
    private final String password = "qwerty123";
    private final boolean isEnabled = true;
    @BeforeEach
    public void setUser() {
        role = new Role(1L, "ADMIN", null);
        User user = new User(1L, "Ivan", username, password, null, isEnabled,
                Collections.singleton(role));
        customUserDetails = new CustomUserDetails(user);
    }
    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> simpleGrantedAuthorities = customUserDetails.getAuthorities();
        Assertions.assertEquals(Collections.singletonList(new SimpleGrantedAuthority(role.getName())), simpleGrantedAuthorities);
    }

    @Test
    public void testGetPassword() {
        Assertions.assertEquals(password, customUserDetails.getPassword());
    }

    @Test
    public void testGetUsername() {
        Assertions.assertEquals(username, customUserDetails.getUsername());
    }

    @Test
    public void testIsEnabled() {
        Assertions.assertEquals(isEnabled, customUserDetails.isEnabled());
    }

    @Test
    public void testIsAccountNonExpired() {
        Assertions.assertEquals(true, customUserDetails.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        Assertions.assertEquals(true, customUserDetails.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        Assertions.assertEquals(true, customUserDetails.isCredentialsNonExpired());
    }
}
