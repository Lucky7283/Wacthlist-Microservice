package com.example.watchlist.service;

import com.example.watchlist.exception.ResourceNotFoundException;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

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
    @PreAuthorize("@accessPolicy.canAccessUser(#p0.userId)")
    public void addComment(CommentDto commentDto) {
        UserEntity user = userRepository.findByIdAndIsDeletedFalse(commentDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", commentDto.getUserId()));
        commentRepository.save(CommentMapper.toEntity(commentDto, user));
    }

    @Override
    @PreAuthorize("@accessPolicy.canAccessComment(#p0)")
    public void removeComment(Long id) {
        CommentEntity entity = commentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", id));
        entity.setDeleted(true);
        commentRepository.save(entity);
    }

    @Override
    @PreAuthorize("@accessPolicy.canAccessComment(#p0)")
    public void changeComment(Long id, CommentDto commentDto) {
        CommentEntity entity = commentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", id));
        if (!java.util.Objects.equals(entity.getUser().getId(), commentDto.getUserId())
                || !java.util.Objects.equals(entity.getAnimeId(), commentDto.getAnimeId())) {
            throw new com.example.watchlist.exception.InvalidRequestException("User and anime associations cannot be changed");
        }
        entity.setComment(commentDto.getComment());
        commentRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByAnimeId(Long animeId) {
        return commentRepository.findByAnimeIdAndIsDeletedFalse(animeId)
                .stream()
                .map(CommentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserIdAndIsDeletedFalse(userId)
                .stream()
                .map(CommentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> getCommentById(Long id) {
        return commentRepository.findByIdAndIsDeletedFalse(id)
                .map(CommentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> getCommentByAnimeIdAndUserId(Long animeId, Long userId) {
        return commentRepository.findFirstByAnimeIdAndUserIdAndIsDeletedFalseOrderByIdDesc(animeId, userId)
                .map(CommentMapper::toDto);
    }
}
