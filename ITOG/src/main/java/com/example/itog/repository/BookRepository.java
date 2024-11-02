package com.example.itog.repository;

import com.example.itog.model.Book;
import com.example.itog.model.Category;
import com.example.itog.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorId(Long authorId);
    List<Book> findByCategories_Id(Long categoryId);
    List<Book> findByPublisher(Publisher publisher);

    @Query("SELECT b FROM Book b JOIN FETCH b.categories")
    List<Book> findAllWithCategories();

    @Query("SELECT b FROM Book b JOIN FETCH b.categories WHERE b.author.id = :authorId")
    List<Book> findByAuthorIdWithCategories(@Param("authorId") Long authorId);

    @Query("SELECT b FROM Book b JOIN FETCH b.categories WHERE b.publisher.id = :publisherId")
    List<Book> findByPublisherWithCategories(@Param("publisherId") Long publisherId);

    @Query("SELECT b FROM Book b JOIN FETCH b.categories WHERE :categoryId MEMBER OF b.categories")
    List<Book> findByCategoryIdWithCategories(@Param("categoryId") Long categoryId);

    @Query("SELECT b FROM Book b JOIN FETCH b.categories WHERE :category MEMBER OF b.categories")
    List<Book> findByCategory(@Param("category") Category category);



}
