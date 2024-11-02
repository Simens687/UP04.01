package com.example.itog.repository;

import com.example.itog.model.BookRequest;
import com.example.itog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
    List<BookRequest> findByUser(User user);
}

