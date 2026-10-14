package com.example.watchlist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class CommentDto {
    private Long id;
    @NotBlank
    @Size(max = 255)
    private String comment;
    @NotNull
    @Positive
    private Long userId;
    @NotNull
    @Positive
    private Long animeId;
    private boolean isDeleted;
}
