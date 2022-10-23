package ru.practicum.explorewithme.event.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Location {
    private Double lat;
    private Double lon;
}
