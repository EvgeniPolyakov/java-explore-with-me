package ru.practicum.explorewithme.user.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.user.model.NewUserDto;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.model.UserDto;

import java.util.HashSet;

import static java.lang.Boolean.FALSE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(NewUserDto userDto) {
        return new User(
                null,
                userDto.getName(),
                userDto.getEmail(),
                new HashSet<>(),
                FALSE
        );
    }
}
