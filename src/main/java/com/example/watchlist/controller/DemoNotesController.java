package com.example.watchlist.controller;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo/notes")
public class DemoNotesController {
    private final ConcurrentSkipListMap<Long, Note> notes = new ConcurrentSkipListMap<>();
    private final AtomicLong nextId = new AtomicLong();

    @GetMapping
    public List<Note> getNotes() {
        return List.copyOf(notes.values());
    }

    @PostMapping
    public ResponseEntity<Note> addNote(@Valid @RequestBody NoteRequest request) {
        Note note = new Note(nextId.incrementAndGet(), request.text().strip());
        notes.put(note.id(), note);
        return ResponseEntity.status(201).body(note);
    }

    public record Note(Long id, String text) {}

    public record NoteRequest(@NotBlank @Size(max = 255) String text) {}
}
