package itis.khabibullina.repository;

import itis.khabibullina.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {

    List<User>  findAllByName(String name);

    Optional<User> findByUsername(String username);

    Optional<User> findByVerificationCode(String verificationCode);

    @Query(value = "select * from users u where u.name = ?1", nativeQuery = true)
    List<User> findAllByName1(String name);

    @Query(value = "select u from User u where u.name = :name")
    List<User> findAllByName2(String name);
}
