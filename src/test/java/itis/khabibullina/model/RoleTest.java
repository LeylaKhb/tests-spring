package itis.khabibullina.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RoleTest {

    @Test
    public void testRolesEquals() {
        Role first = new Role(1L, "ADMIN", null);
        Role second = new Role(1L, "ADMIN", null);
        Assertions.assertTrue(first.equals(second));
    }

    @Test
    public void testHashCode() {
        Role role = new Role(1L, "ADMIN", null);
        Assertions.assertEquals(Objects.hash(1L, "ADMIN", null), role.hashCode());
    }
}
