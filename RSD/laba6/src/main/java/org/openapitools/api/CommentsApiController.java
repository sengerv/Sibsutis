package org.openapitools.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.Comment;
import org.openapitools.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("${openapi.commentManagement.base-path:/api/v1}")
public class CommentsApiController implements CommentsApi {

    private final NativeWebRequest request;
    private final CommentService commentService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    /**
     * Получить все комментарии
     */
    @Override
    public ResponseEntity<List<Map<String, Object>>> getAllComments(@RequestParam(value = "parentId", required = false) String parentId) {
        try {
            Integer parentIdInt = parentId != null ? Integer.valueOf(parentId) : null;

            List<Comment> comments = parentIdInt != null
                    ? commentService.getCommentsByParentId(parentIdInt)
                    : commentService.getAllComments();

            List<Map<String, Object>> commentsAsMap = comments.stream()
                    .map(this::convertToMap)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(commentsAsMap);
        } catch (Exception e) {
            log.error("Error fetching comments", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error fetching comments",
                    e
            );
        }
    }

    /**
     * Создать новый комментарий
     */
    @Override
    public ResponseEntity<Void> createComment(@Valid @RequestBody Map<String, Object> commentData) {
        try {
            Comment comment = convertToComment(commentData);
            Comment createdComment = commentService.createComment(comment);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResponseStatusException e) {
            log.warn("Create failed: {}", e.getReason());
            throw e;
        } catch (Exception e) {
            log.error("Error creating comment", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error creating comment",
                    e
            );
        }
    }

    /**
     * Получить комментарий по ID
     */
    @Override
    public ResponseEntity<Map<String, Object>> getCommentById(@PathVariable("id") String id) {
        try {
            Integer commentId = Integer.valueOf(id);
            Comment comment = commentService.getCommentById(commentId);
            Map<String, Object> commentAsMap = convertToMap(comment);
            return ResponseEntity.ok(commentAsMap);
        } catch (ResponseStatusException e) {
            log.warn("Comment not found: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Error fetching comment", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error fetching comment",
                    e
            );
        }
    }

    /**
     * Удалить комментарий
     */
    @Override
    public ResponseEntity<Void> deleteComment(@PathVariable("id") String id) {
        try {
            Integer commentId = Integer.valueOf(id);
            if (commentService.deleteById(commentId)) {
                log.info("Comment deleted: {}", id);
                return ResponseEntity.noContent().build();
            }
            log.warn("Comment not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting comment", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting comment",
                    e
            );
        }
    }

    /**
     * Преобразование Comment в Map<String, Object>
     */
    /**
     * Преобразование Comment в Map<String, Object>
     */
    private Map<String, Object> convertToMap(Comment comment) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", comment.getId());
        map.put("text", comment.getText());
        map.put("parentId", comment.getParentId());
        map.put("authorId", comment.getAuthorId());
        map.put("postId", comment.getPostId());
        map.put("createdAt", comment.getCreatedAt().toString()); // Преобразуем LocalDateTime в строку
        return map;
    }

    /**
     * Преобразование Map<String, Object> в Comment
     */
    private Comment convertToComment(Map<String, Object> map) {
        Comment comment = new Comment((String) map.get("text")); // Создаем комментарий с текстом
        comment.setId((Integer) map.get("id"));
        comment.setParentId((Integer) map.get("parentId"));
        comment.setAuthorId((Integer) map.get("authorId"));
        comment.setPostId((Integer) map.get("postId"));
        comment.setCreatedAt(LocalDateTime.parse((String) map.get("createdAt"))); // Преобразуем строку обратно в LocalDateTime
        return comment;
    }
}