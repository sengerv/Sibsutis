package org.openapitools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.Comment;
import org.openapitools.repository.CommentRepository;
import org.openapitools.service.CommentService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment createTestComment(int id, String text) {
        return new Comment(id, text, null); // Создание тестового комментария
    }

    @Test
    void getAllComments_shouldReturnAllComments() {
        // Arrange
        Comment comment1 = createTestComment(1, "Test1");
        Comment comment2 = createTestComment(2, "Test2");
        when(commentRepository.findAll()).thenReturn(List.of(comment1, comment2));

        // Act
        List<Comment> result = commentService.getAllComments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Test1", result.get(0).getText());
        verify(commentRepository, times(1)).findAll();
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void getCommentById_whenExists_shouldReturnComment() {
        // Arrange
        int id = 1;
        Comment expectedComment = createTestComment(id, "Test");
        when(commentRepository.findById(id)).thenReturn(Optional.of(expectedComment));

        // Act
        Comment result = commentService.getCommentById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Test", result.getText());
        verify(commentRepository, times(1)).findById(id);
    }

    @Test
    void getCommentById_whenNotExists_shouldReturnNull() {
        // Arrange
        int id = 999;
        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Comment result = commentService.getCommentById(id);

        // Assert
        assertNull(result);
        verify(commentRepository, times(1)).findById(id);
    }

    @Test
    void createComment_shouldSaveAndReturnComment() {
        // Arrange
        Comment newComment = createTestComment(0, "New");
        Comment savedComment = createTestComment(1, "New");
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        // Act
        Comment result = commentService.createComment(newComment);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("New", result.getText());
        verify(commentRepository, times(1)).save(newComment);
    }

    @Test
    void deleteById_shouldCallRepository() {
        // Arrange
        int id = 1;
        doNothing().when(commentRepository).deleteById(id);

        // Act
        commentService.deleteById(id);

        // Assert
        verify(commentRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(commentRepository);
    }
}