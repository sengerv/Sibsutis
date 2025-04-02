package org.openapitools.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.Comment;
import org.openapitools.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        try {
            List<Comment> comments = commentRepository.findAll();
            log.debug("Retrieved {} comments", comments.size());
            return comments;
        } catch (Exception e) {
            log.error("Failed to retrieve comments", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve comments",
                    e
            );
        }
    }

    public Comment getCommentById(Integer id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Comment with id {} not found", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Comment not found with id: " + id
                    );
                });
    }

    public boolean deleteById(Integer id) {
        try {
            if (!commentRepository.existsById(id)) {
                log.warn("Comment with id {} not found for deletion", id);
                return false;
            }
            commentRepository.deleteById(id);
            log.info("Comment with id {} deleted successfully", id);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete comment with id {}", id, e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to delete comment",
                    e
            );
        }
    }

    public Comment createComment(Comment comment) {
        try {
            Objects.requireNonNull(comment, "Comment cannot be null");
            comment.setCreatedAt(LocalDateTime.now());
            Comment savedComment = commentRepository.save(comment);
            log.info("Created new comment with id {}", savedComment.getId());
            return savedComment;
        } catch (Exception e) {
            log.error("Failed to create comment", e);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid comment data: " + e.getMessage(),
                    e
            );
        }
    }

    public Comment updateComment(Integer id, Comment updatedComment) {
        try {
            Objects.requireNonNull(updatedComment, "Updated comment cannot be null");

            Comment existingComment = getCommentById(id);

            validateCommentUpdate(existingComment, updatedComment);

            existingComment.setText(updatedComment.getText());

            Comment savedComment = commentRepository.save(existingComment);
            log.info("Updated comment with id {}", id);
            return savedComment;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update comment with id {}", id, e);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid update data: " + e.getMessage(),
                    e
            );
        }
    }

    private void validateCommentUpdate(Comment existing, Comment updated) {
        if (!Objects.equals(existing.getAuthorId(), updated.getAuthorId())) {
            log.warn("Attempt to change authorId from {} to {} for comment {}",
                    existing.getAuthorId(), updated.getAuthorId(), existing.getId());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot change comment author"
            );
        }

        if (!Objects.equals(existing.getPostId(), updated.getPostId())) {
            log.warn("Attempt to change postId from {} to {} for comment {}",
                    existing.getPostId(), updated.getPostId(), existing.getId());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot change associated post"
            );
        }

        if (!Objects.equals(existing.getCreatedAt(), updated.getCreatedAt())) {
            log.warn("Attempt to change createdAt for comment {}", existing.getId());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot change creation timestamp"
            );
        }
    }

    public List<Comment> getCommentsByParentId(Integer parentId) {
        try {
            List<Comment> comments = commentRepository.findByParentId(parentId);
            log.debug("Retrieved {} comments for parentId {}", comments.size(), parentId);
            return comments;
        } catch (Exception e) {
            log.error("Failed to retrieve comments by parentId {}", parentId, e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve comments by parentId",
                    e
            );
        }
    }
}