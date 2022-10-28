package ru.practicum.explorewithme.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewUserDto {
    @NotBlank
    @Size(max = 255)
    private String name;
    @NotNull
    @Email
    @Size(max = 255)
    private String email;
}
