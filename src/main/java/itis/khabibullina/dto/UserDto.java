package itis.khabibullina.dto;

import itis.khabibullina.model.User;

import java.util.Objects;

public class UserDto {

    public String name;

    public UserDto(String name) {
        this.name = name;
    }

    public static UserDto fromEntity(User user) {
        return new UserDto(user.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(name, userDto.name);
    }
}
