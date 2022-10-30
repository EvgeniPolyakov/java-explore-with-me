package ru.practicum.explorewithme.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@AllArgsConstructor
@NoArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "app_id")
    private App app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;

    public App getApp() {
        return app;
    }

    public String getUri() {
        return uri;
    }
}
