package org.openapitools.repository;

import org.openapitools.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // Найти все комментарии для конкретного поста
    List<Comment> findByPostId(Integer postId);

    // Найти все комментарии конкретного автора
    List<Comment> findByAuthorId(Integer authorId);

    // Найти комментарии по содержанию (регистронезависимый поиск)
    List<Comment> findByTextContainingIgnoreCase(String textPart); // Обновлено с content на text

    // Найти комментарии для поста с пагинацией
    @Query("SELECT c FROM Comment c WHERE c.postId = :postId ORDER BY c.id DESC")
    List<Comment> findLatestByPostId(@Param("postId") Integer postId);

    // Обновить содержание комментария
    @Modifying
    @Query("UPDATE Comment c SET c.text = :text WHERE c.id = :id") // Обновлено с content на text
    int updateCommentContent(@Param("id") Integer id, @Param("text") String text);

    // Проверить существование комментария по ID
    boolean existsById(Integer id);

    // Найти комментарий по ID (уже есть в JpaRepository, но можно переопределить)
    @Override
    Optional<Comment> findById(Integer id);

    // Найти все комментарии по parentId
    List<Comment> findByParentId(Integer parentId);
}