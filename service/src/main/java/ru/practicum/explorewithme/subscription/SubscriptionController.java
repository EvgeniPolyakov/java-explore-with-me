package ru.practicum.explorewithme.subscription;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.model.EventShortDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{id}/friends")
public class SubscriptionController {
    public static final String ID_PATH_VARIABLE_KEY = "id";
    public static final String FRIEND_ID_PATH_VARIABLE_KEY = "friendId";

    private final SubscriptionService subscriptionService;

    @GetMapping("/{friendId}/events")
    public List<EventShortDto> getFriendEvents(
            @PathVariable(ID_PATH_VARIABLE_KEY) Long userId,
            @PathVariable(FRIEND_ID_PATH_VARIABLE_KEY) Long friendId,
            @RequestParam(required = false, defaultValue = "false") boolean excludeOwn,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Positive int size
    ) {
        log.debug("Получен запрос GET по пути /users/{}/friends/{}/events", userId, friendId);
        return subscriptionService.getFriendEvents(userId, friendId, excludeOwn, from, size);
    }

    @GetMapping("/events")
    public List<EventShortDto> getAllFriendsEvents(
            @PathVariable(ID_PATH_VARIABLE_KEY) Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean excludeOwn,
            @RequestParam(required = false, defaultValue = "false") boolean excludeMutual,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Positive int size
    ) {
        log.debug("Получен запрос GET по пути /users/{}/friends/events", userId);
        return subscriptionService.getAllUserFriendsAvailableEvents(userId, excludeOwn, excludeMutual, from, size);
    }

    @PutMapping("/{friendId}")
    public void addFriend(
            @PathVariable(ID_PATH_VARIABLE_KEY) @Positive @NotNull Long userId,
            @PathVariable(FRIEND_ID_PATH_VARIABLE_KEY) @Positive @NotNull Long friendId
    ) {
        log.debug("Получен запрос PUT по пути /users/{}/friends/{}", userId, friendId);
        subscriptionService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{friendId}")
    public void deleteFriend(
            @PathVariable(ID_PATH_VARIABLE_KEY) @Positive @NotNull Long userId,
            @PathVariable(FRIEND_ID_PATH_VARIABLE_KEY) @Positive @NotNull Long friendId
    ) {
        log.debug("Получен запрос DELETE по пути /users/{}/friends/{}", userId, friendId);
        subscriptionService.deleteFriend(userId, friendId);
    }

    @PatchMapping("/privacy")
    public void setPrivacy(
            @PathVariable(ID_PATH_VARIABLE_KEY) @Positive @NotNull Long userId,
            @RequestParam boolean isPrivate
    ) {
        log.debug("Получен запрос PUT по пути /admin/users/{}/friends/{}", userId, isPrivate);
        subscriptionService.setPrivacy(userId, isPrivate);
    }
}