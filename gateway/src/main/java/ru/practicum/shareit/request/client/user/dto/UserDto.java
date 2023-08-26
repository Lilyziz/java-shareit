package ru.practicum.shareit.request.client.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;
}