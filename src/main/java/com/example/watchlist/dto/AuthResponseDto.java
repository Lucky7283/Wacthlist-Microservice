package com.example.watchlist.dto;

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
public class AuthResponseDto {
    private String token;
    @Builder.Default
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserDto user;
}
