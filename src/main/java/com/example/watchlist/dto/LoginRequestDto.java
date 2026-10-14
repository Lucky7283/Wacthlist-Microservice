package com.example.watchlist.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.validation.constraints.NotBlank;
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
public class LoginRequestDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
