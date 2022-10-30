package ru.practicum.explorewithme.user.service;

import ru.practicum.explorewithme.user.model.NewUserDto;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.model.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll(List<Long> ids, int from, int size);

    User getById(Long id);

    UserDto add(NewUserDto user);

    void delete(Long id);
}
