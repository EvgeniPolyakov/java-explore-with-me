package ru.practicum.explorewithme.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.user.model.NewUserDto;
import ru.practicum.explorewithme.user.model.UserDto;
import ru.practicum.explorewithme.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserController {
    public static final String ID_PATH_VARIABLE_KEY = "id";

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllById(@RequestParam List<Long> ids,
                                    @RequestParam(required = false, defaultValue = "0") int from,
                                    @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос GET по пути /admin/users");
        return userService.getAll(ids, from, size);
    }

    @PostMapping
    public UserDto add(@RequestBody NewUserDto userDto) {
        log.info("Получен запрос POST по пути /admin/users для добавления пользователя: {}", userDto);
        return userService.add(userDto);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Получен запрос DELETE по пути /admin/users по id {}", id);
        userService.delete(id);
    }
}