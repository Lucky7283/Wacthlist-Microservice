package com.example.watchlist.dto;

import com.example.watchlist.entity.UserRoles;
import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(onConstructor_ = @JsonCreator)
@AllArgsConstructor
@Builder
public class RegisterRequestDto {
    @NotBlank
    @Size(max = 255)
    private String username;

    @NotBlank
    @Size(min = 4, max = 72)
    private String password;

    @Builder.Default
    private UserRoles role = UserRoles.USER;
}
