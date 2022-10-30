package ru.practicum.explorewithme.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "apps")
@AllArgsConstructor
@NoArgsConstructor
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "app_name")
    private String appName;

    public String getAppName() {
        return appName;
    }
}
