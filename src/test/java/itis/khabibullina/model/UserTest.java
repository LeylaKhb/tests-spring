package itis.khabibullina.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserTest {
    @Test
    public void testUsersEquals() {
        Role role = new Role(1L, "ADMIN", null);
        User first = new User(1L, "Ivan", "ivan@mail.ru", "qwerty123", null, true,
                Collections.singleton(role));

        User second = new User(1L, "Ivan", "ivan@mail.ru", "qwerty123", null, true,
                Collections.singleton(role));

        Assertions.assertTrue(first.equals(second));
    }

    @Test
    public void testHashCode() {
        Role role = new Role(1L, "ADMIN", null);
        User user = new User(1L, "Ivan", "ivan@mail.ru", "qwerty123", null, true,
                Collections.singleton(role));

        Assertions.assertEquals(Objects.hash(1L, "Ivan", "ivan@mail.ru", "qwerty123", null, true,
                Collections.singleton(role)), user.hashCode());
    }
}
