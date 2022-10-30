package ru.practicum.explorewithme.request.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.request.model.RequestDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getRequester().getId(),
                request.getEvent().getId(),
                request.getCreated(),
                request.getStatus()
        );
    }
}
