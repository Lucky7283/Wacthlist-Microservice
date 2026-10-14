package com.example.watchlist.dto;

import com.example.watchlist.entity.WatchListStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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
public class WatchListDto {
    private Long id;
    @NotNull
    @Positive
    private Long userId;
    @NotNull
    @Positive
    private Long animeId;
    @NotNull
    private WatchListStatus status;
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private double rating;
    private boolean isDeleted;
}
