package com.example.watchlist.service.Interfaces;

import com.example.watchlist.dto.CommentDto;

public interface CommentService {
    void addComment(CommentDto commentDto);

    void removeComment(Long id);

    void changeComment(Long id, CommentDto commentDto);
}
