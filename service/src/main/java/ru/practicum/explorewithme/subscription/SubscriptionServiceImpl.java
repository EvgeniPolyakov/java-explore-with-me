package ru.practicum.explorewithme.subscription;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.model.EventShortDto;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.exception.ForbiddenException;
import ru.practicum.explorewithme.request.model.RequestDto;
import ru.practicum.explorewithme.request.service.RequestService;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private static final String NOT_A_FRIEND_MESSAGE = "Пользователь %s не входит в число друзей пользователя %s.";
    private static final String ACCESS_DENIED_MESSAGE = "Пользователь %s закрыл доступ к событиям со своим участием.";
    private static final String FRIEND_ALREADY_ADDED_MESSAGE = "Пользователь %s уже находится в списке друзей.";

    private final UserService userService;
    private final EventService eventService;
    private final RequestService requestService;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getFriendEvents(Long userId, Long friendId, boolean excludeOwn, int from, int size) {
        log.info("Получение пользователем с id {} событий с участием пользователя с id {}", userId, friendId);
        User user = userService.getById(userId);
        User friend = userService.getById(friendId);
        if (!user.getFriends().contains(friend)) {
            throw new ForbiddenException(String.format(NOT_A_FRIEND_MESSAGE, friendId, userId));
        }
        if (friend.getPrivateMode()) {
            throw new ForbiddenException(String.format(ACCESS_DENIED_MESSAGE, friendId));
        }
        List<EventShortDto> eventsCollected = eventService.getEventsUserCreatedOrJoined(friendId, from, size);
        if (excludeOwn) {
            return eventsCollected.stream()
                    .filter(e -> !e.getInitiator().equals(user))
                    .collect(Collectors.toList());
        }
        return eventsCollected;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAllUserFriendsAvailableEvents(Long userId, boolean excludeOwn, boolean excludeMutual,
                                                                int from, int size) {
        log.info("Получение всех событий с участием друзей пользователя с id {}", userId);
        User user = userService.getById(userId);
        Set<User> friends = user.getFriends();
        List<Long> friendIds = friends.stream()
                .filter(u -> !u.getPrivateMode())
                .map(User::getId)
                .collect(Collectors.toList());
        List<EventShortDto> eventsCollected = eventService.getAllUserFriendsEvents(friendIds, from, size);
        if (excludeOwn) {
            eventsCollected = eventsCollected.stream()
                    .filter(e -> !e.getInitiator().equals(user))
                    .collect(Collectors.toList());
        }
        if (excludeMutual) {
            List<RequestDto> userRequests = requestService.getAll(userId);
            List<Long> userRequestIds = userRequests.stream()
                    .map(RequestDto::getEvent)
                    .collect(Collectors.toList());
            eventsCollected = eventsCollected.stream()
                    .filter(e -> !userRequestIds.contains(e.getId()))
                    .collect(Collectors.toList());
        }
        return eventsCollected;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        log.info("Добавление подписки пользователя с id {} на пользователя с id {}", userId, friendId);
        User user = userService.getById(userId);
        User friend = userService.getById(friendId);
        if (friend.getPrivateMode()) {
            throw new ForbiddenException(String.format(ACCESS_DENIED_MESSAGE, friendId));
        }
        if (user.getFriends().contains(friend)) {
            throw new ForbiddenException(String.format(FRIEND_ALREADY_ADDED_MESSAGE, friendId));
        }
        user.getFriends().add(friend);
        log.info("Пользователь с id {} подписался на события пользователя с id {}", userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        log.info("Удаление подписки пользователя с id {} на пользователя с id {}", userId, friendId);
        User user = userService.getById(userId);
        User friend = userService.getById(friendId);
        user.getFriends().remove(friend);
        log.info("Пользователь с id {} удалил подписку на события пользователя с id {}", userId, friendId);
    }

    @Override
    public void setPrivacy(Long id, boolean isPrivate) {
        log.info("Присвоение пользователю id {} уровня приватности: {}", id, isPrivate);
        User user = userService.getById(id);
        user.setPrivateMode(isPrivate);
        log.info("Пользователь с id {} присвоил уровень приватности: {}", id, isPrivate);
    }
}
