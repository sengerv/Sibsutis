package org.openapitools.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity // Указывает, что это JPA-сущность
@Table(name = "comments") // Указывает имя таблицы в базе данных
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация ID
  private Integer id;

  @Column(nullable = false)
  private String text;

  private Integer parentId; // ID родительского комментария

  private Integer authorId; // ID автора

  private Integer postId; // ID поста

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  // Конструкторы, геттеры и сеттеры
  public Comment(long l, String s, Object o, Object object) {}

  public Comment(String text) {
    this.text = text;
    this.createdAt = LocalDateTime.now();
  }

  public Comment(int i, String test1, Object o) {
  }

  // Геттеры и сеттеры
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Integer getParentId() {
    return parentId;
  }

  public void setParentId(Integer parentId) {
    this.parentId = parentId;
  }

  public Integer getAuthorId() {
    return authorId;
  }

  public void setAuthorId(Integer authorId) {
    this.authorId = authorId;
  }

  public Integer getPostId() {
    return postId;
  }

  public void setPostId(Integer postId) {
    this.postId = postId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}