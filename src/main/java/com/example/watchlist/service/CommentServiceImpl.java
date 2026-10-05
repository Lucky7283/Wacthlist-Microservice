package com.example.watchlist.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.watchlist.dto.CommentDto;
import com.example.watchlist.entity.CommentEntity;
import com.example.watchlist.entity.UserEntity;
import com.example.watchlist.mapper.CommentMapper;
import com.example.watchlist.repository.CommentRepository;
import com.example.watchlist.repository.UserRepository;
import com.example.watchlist.service.Interfaces.CommentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public void addComment(CommentDto commentDto) {
        UserEntity user = userRepository.findById(commentDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + commentDto.getUserId()));
        commentRepository.save(CommentMapper.toEntity(commentDto, user));
    }

    @Override
    public void removeComment(Long id) {
        CommentEntity entity = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        entity.setDeleted(true);
        commentRepository.save(entity);
    }

    @Override
    public void changeComment(Long id, CommentDto commentDto) {
        CommentEntity entity = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        entity.setComment(commentDto.getComment());
        commentRepository.save(entity);
    }
}
