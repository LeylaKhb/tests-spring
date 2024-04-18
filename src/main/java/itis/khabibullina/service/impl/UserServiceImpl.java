package itis.khabibullina.service.impl;

import itis.khabibullina.dto.CreateUserRequestDto;
import itis.khabibullina.dto.UserDto;
import itis.khabibullina.model.User;
import itis.khabibullina.repository.UserRepository;
import itis.khabibullina.service.EmailServices;
import itis.khabibullina.service.UserService;
import freemarker.template.TemplateException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final EmailServices emailServices;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder, EmailServices emailServices) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.emailServices = emailServices;
    }


    @Override
    public List<UserDto> findAllByName(String name) {
        return userRepository.findAllByName(name)
                .stream().map(u -> new UserDto(u.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto create(CreateUserRequestDto dto) throws TemplateException, IOException {
        User user = new User();
        user.setName(dto.name);
        user.setUsername(dto.email);
        user.setPassword(encoder.encode(dto.password));
        emailServices.sendEmail(dto.email);
        return UserDto.fromEntity(userRepository.save(user));
    }

    @Override
    public boolean verify(String code) {
        Optional<User> user = userRepository.findByVerificationCode(code);
        if (user.isPresent()) {
            User u = user.get();
            u.setVerificationCode(null);
            u.setEnabled(true);
            userRepository.save(u);
            return true;
        }
        return false;
    }


}
