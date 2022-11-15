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
    private static final String NOT_A_FRIEND_MESSAGE = "User %s is not present in user %s subscription list";
    private static final String ACCESS_DENIED_MESSAGE = "User %s has denied access to his events";
    private static final String FRIEND_ALREADY_ADDED_MESSAGE = "User %s is already present in subscription list";

    private final UserService userService;
    private final EventService eventService;
    private final RequestService requestService;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getFriendEvents(Long userId, Long friendId, boolean excludeOwn, int from, int size) {
        log.info("User {} getting events attended by user {}", userId, friendId);
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
        log.info("User {} getting events attended by all users from his subscription list", userId);
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
        log.info("Adding user {} to user {} subscription list", friendId, userId);
        User user = userService.getById(userId);
        User friend = userService.getById(friendId);
        if (friend.getPrivateMode()) {
            throw new ForbiddenException(String.format(ACCESS_DENIED_MESSAGE, friendId));
        }
        if (user.getFriends().contains(friend)) {
            throw new ForbiddenException(String.format(FRIEND_ALREADY_ADDED_MESSAGE, friendId));
        }
        user.getFriends().add(friend);
        log.info("User {} has been added to user {} subscription list", friendId, userId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        log.info("Removing user {} from user {} subscription list", friendId, userId);
        User user = userService.getById(userId);
        User friend = userService.getById(friendId);
        user.getFriends().remove(friend);
        log.info("User {} has removed user {} from his subscription list", userId, friendId);
    }

    @Override
    public void setPrivacy(Long id, boolean isPrivate) {
        log.info("Assigning private mode status {} for user {}", isPrivate, id);
        User user = userService.getById(id);
        user.setPrivateMode(isPrivate);
        log.info("User {} has assigned private mode status: {}", id, isPrivate);
    }
}
