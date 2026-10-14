package com.example.watchlist.dto;

import com.example.watchlist.entity.UserRoles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;

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
public class UserDto {
    private Long id;
    @NotBlank
    @Size(max = 255)
    private String username;
    @NotNull
    private UserRoles role;
    private boolean isDeleted;
}
