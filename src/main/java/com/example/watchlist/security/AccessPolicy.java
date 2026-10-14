package com.example.watchlist.security;

import com.example.watchlist.entity.UserRoles;
import com.example.watchlist.repository.CommentRepository;
import com.example.watchlist.repository.UserRepository;
import com.example.watchlist.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("accessPolicy")
@RequiredArgsConstructor
public class AccessPolicy {
    private final UserRepository users;
    private final CommentRepository comments;
    private final WatchlistRepository watchlists;

    public boolean isAdmin() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && authentication.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    public boolean canAccessUser(Long userId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return isAdmin() || (authentication != null && authentication.isAuthenticated()
                && userId != null && userId.equals(authentication.getPrincipal()));
    }

    public boolean canAccessUsername(String username) {
        return isAdmin() || users.findByUsernameAndIsDeletedFalse(username)
                .map(user -> canAccessUser(user.getId())).orElse(false);
    }

    // Missing IDs proceed to the service's normal 404 response.
    public boolean canAccessComment(Long id) {
        return isAdmin() || comments.findByIdAndIsDeletedFalse(id)
                .map(comment -> canAccessUser(comment.getUser().getId())).orElse(true);
    }

    public boolean canAccessWatchlist(Long id) {
        return isAdmin() || watchlists.findByIdAndIsDeletedFalse(id)
                .map(entry -> canAccessUser(entry.getUser().getId())).orElse(true);
    }

    public boolean canEditUser(Long id, UserRoles requestedRole) {
        return isAdmin() || (canAccessUser(id) && users.findByIdAndIsDeletedFalse(id)
                .map(user -> user.getRole() == requestedRole).orElse(true));
    }
}
