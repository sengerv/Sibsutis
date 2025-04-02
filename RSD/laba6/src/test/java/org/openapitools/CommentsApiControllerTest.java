package org.openapitools;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.openapitools.api.CommentsApiController;
import org.openapitools.model.Comment;
import org.openapitools.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentsApiController.class)
class CommentsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllComments() throws Exception {
        // Arrange
        Comment comment1 = new Comment(1L, "Comment 1", null, null);
        Comment comment2 = new Comment(2L, "Comment 2", null, null);

        List<Comment> comments = List.of(comment1, comment2);
        when(commentService.getAllComments()).thenReturn(comments);

        // Act & Assert
        mockMvc.perform(get("/api/v1/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].text").value("Comment 1")) // используем правильное имя поля
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].text").value("Comment 2"))
                .andDo(print());
    }

    @Test
    void testCreateComment() throws Exception {
        // Arrange
        Comment requestComment = new Comment(null);
        Comment responseComment = new Comment(1L, "New Comment", null, null);

        when(commentService.createComment(any(Comment.class))).thenReturn(responseComment);

        // Act & Assert
        mockMvc.perform(post("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestComment)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("New Comment")) // используем правильное имя поля
                .andDo(print());
    }
}