package itis.khabibullina.controller;

import itis.khabibullina.dto.CreateUserRequestDto;
import itis.khabibullina.dto.UserDto;
import itis.khabibullina.service.UserService;
import freemarker.template.TemplateException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @GetMapping("/users")
    public List<UserDto> findByName(@RequestParam String name) {
        return userService.findAllByName(name);
    }

    @PostMapping("/user")
    public String create(@ModelAttribute CreateUserRequestDto user) throws TemplateException, IOException {
        userService.create(user);
        return "sign_up_success";
    }

    @GetMapping("/verification")
    public String verify(@Param("code") String code) {
        if (userService.verify(code)) {
            return "verification_success";
        }
        return "verification_failed";
    }
}
