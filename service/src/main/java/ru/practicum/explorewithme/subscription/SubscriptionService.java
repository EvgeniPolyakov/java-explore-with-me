package ru.practicum.explorewithme.subscription;

import ru.practicum.explorewithme.event.model.EventShortDto;

import java.util.List;

public interface SubscriptionService {
    List<EventShortDto> getFriendEvents(Long userId, Long friendId, int from, int size);

    List<EventShortDto> getAllUserFriendsAvailableEvents(Long userId, int from, int size);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    void setPrivacy(Long userId, boolean isPrivate);
}
