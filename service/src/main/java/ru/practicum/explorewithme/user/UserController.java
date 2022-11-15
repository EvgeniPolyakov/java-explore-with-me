package ru.practicum.explorewithme.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.user.model.NewUserDto;
import ru.practicum.explorewithme.user.model.UserDto;
import ru.practicum.explorewithme.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/users")
public class UserController {
    public static final String ID_PATH_VARIABLE_KEY = "id";

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllById(@RequestParam List<Long> ids,
                                    @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
                                    @RequestParam(required = false, defaultValue = "10") @Positive int size) {
        log.info("Received GET request on /admin/users");
        return userService.getAll(ids, from, size);
    }

    @PostMapping
    public UserDto add(@Valid @RequestBody NewUserDto userDto) {
        log.info("Received POST request on /admin/users to add user: {}", userDto);
        return userService.add(userDto);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Received DELETE request on /admin/users with id {}", id);
        userService.delete(id);
    }
}