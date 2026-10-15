package com.example.watchlist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.watchlist.dto.CommentDto;
import com.example.watchlist.service.Interfaces.CommentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addComment(@Valid @RequestBody CommentDto commentDto) {
        commentService.addComment(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removeComment(@PathVariable @Positive Long id) {
        commentService.removeComment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> changeComment(@PathVariable @Positive Long id, @Valid @RequestBody CommentDto commentDto) {
        commentService.changeComment(id, commentDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable @Positive Long id) {
        return commentService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/anime/{animeId}")
    public ResponseEntity<List<CommentDto>> getCommentsByAnimeId(@PathVariable @Positive Long animeId) {
        return ResponseEntity.ok(commentService.getCommentsByAnimeId(animeId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDto>> getCommentsByUserId(@PathVariable @Positive Long userId) {
        return ResponseEntity.ok(commentService.getCommentsByUserId(userId));
    }

    @GetMapping("/anime/{animeId}/user/{userId}")
    public ResponseEntity<CommentDto> getCommentByAnimeIdAndUserId(
            @PathVariable @Positive Long animeId,
            @PathVariable @Positive Long userId) {
        return commentService.getCommentByAnimeIdAndUserId(animeId, userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
