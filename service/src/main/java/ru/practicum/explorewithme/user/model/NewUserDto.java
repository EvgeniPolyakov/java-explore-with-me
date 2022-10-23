package ru.practicum.explorewithme.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class NewUserDto {
    @NotBlank
    private String name;
    @Email
    private String email;
}
