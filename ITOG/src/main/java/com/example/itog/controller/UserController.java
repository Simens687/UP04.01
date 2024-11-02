package com.example.itog.controller;

import com.example.itog.model.*;
import com.example.itog.repository.*;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private BookRequestRepository bookRequestRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    public String HELLO() {
        return "user/user";
    }

    @GetMapping("/books")
    public String showBooks(Model model) {
        List<Book> books = bookRepository.findAllWithCategories();
        model.addAttribute("books", books);
        return "user/books";
    }

    @GetMapping("/authors")
    public String showAuthors(Model model) {
        List<Author> authors = authorRepository.findAll();
        model.addAttribute("authors", authors);
        return "user/authors";
    }

    @GetMapping("/authors/{authorId}/books")
    public String showBooksByAuthor(@PathVariable Long authorId, Model model) {
        List<Book> books = bookRepository.findByAuthorIdWithCategories(authorId);
        model.addAttribute("books", books);
        return "user/authorBooks";
    }

    @GetMapping("/categories")
    public String showCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "user/categories";
    }

    @GetMapping("/categories/{categoryId}/books")
    public String getBooksByCategory(@PathVariable Long categoryId, Model model) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Нет такой категории: "));
        List<Book> books = bookRepository.findByCategory(category);
        model.addAttribute("books", books);
        return "user/categoryBooks";
    }

    @GetMapping("/publishers")
    public String showPublishers(Model model) {
        List<Publisher> publishers = publisherRepository.findAll();
        model.addAttribute("publishers", publishers);
        return "user/publishers";
    }

    @GetMapping("/publishers/{publisherId}/books")
    public String showBooksByPublisher(@PathVariable Long publisherId, Model model) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID издателя: " + publisherId));
        List<Book> books = bookRepository.findByPublisherWithCategories(publisherId);
        model.addAttribute("books", books);
        return "user/publisherBooks";
    }

    @GetMapping("/my-requests")
    public String showMyRequests(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        List<BookRequest> requests = bookRequestRepository.findByUser(user);
        model.addAttribute("requests", requests);
        return "user/myRequests";
    }

    @PostMapping("/requests/{requestId}/delete")
    public String deleteRequest(@PathVariable Long requestId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        BookRequest bookRequest = bookRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Запрос не найден"));

        if (bookRequest.getUser().equals(user)) {
            bookRequestRepository.delete(bookRequest);
        }
        return "redirect:/users/my-requests";
    }

    @PostMapping("/books/{bookId}/request")
    public String requestBook(@PathVariable Long bookId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        BookRequest bookRequest = new BookRequest();
        bookRequest.setUser(user);
        bookRequest.setBook(book);
        bookRequest.setStatus(RequestStatus.PENDING);
        bookRequest.setRequestDate(LocalDate.now());
        bookRequestRepository.save(bookRequest);

        return "redirect:/users/my-requests";
    }
}
