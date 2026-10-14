package com.example.watchlist.service.Interfaces;

import java.util.List;
import java.util.Optional;

import com.example.watchlist.dto.CommentDto;

public interface CommentService {
    void addComment(CommentDto commentDto);

    void removeComment(Long id);

    void changeComment(Long id, CommentDto commentDto);

    List<CommentDto> getCommentsByAnimeId(Long animeId);

    List<CommentDto> getCommentsByUserId(Long userId);

    Optional<CommentDto> getCommentById(Long id);

    Optional<CommentDto> getCommentByAnimeIdAndUserId(Long animeId, Long userId);
}
