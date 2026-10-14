package com.example.watchlist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.watchlist.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByIsDeletedFalse();

    Optional<UserEntity> findByIdAndIsDeletedFalse(Long id);

    Optional<UserEntity> findByUsernameAndIsDeletedFalse(String username);

    boolean existsByUsernameAndIsDeletedFalse(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);
}
