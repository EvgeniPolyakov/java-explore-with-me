package ru.practicum.explorewithme.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.user.UserRepository;
import ru.practicum.explorewithme.user.model.NewUserDto;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.model.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String USER_NOT_FOUND_MESSAGE = "User with id %s has not been found";

    private final UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll(List<Long> ids, int from, int size) {
        log.info("Getting the list with all users");
        Pageable pageable = PageRequest.of(from, size);
        List<User> users = repository.findUsersByIdIn(ids, pageable);
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        log.info("Getting user with id {}", id);
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    public UserDto add(NewUserDto userDto) {
        log.info("Adding user: {}", userDto);
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(repository.save(user));
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting user with id {}", id);
        User user = getById(id);
        repository.delete(user);
        log.info("User with id {} has been deleted", id);
    }
}
