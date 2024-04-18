package itis.khabibullina.service;

import itis.khabibullina.dto.CreateUserRequestDto;
import itis.khabibullina.dto.UserDto;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;

public interface UserService {

    List<UserDto> findAllByName(String name);

    UserDto create(CreateUserRequestDto dto) throws TemplateException, IOException;

    boolean verify(String code);
}
