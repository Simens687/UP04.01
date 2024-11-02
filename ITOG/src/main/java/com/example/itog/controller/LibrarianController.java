package com.example.itog.controller;

import com.example.itog.model.*;
import com.example.itog.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/librarian")
public class LibrarianController {

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
    private InventoryRepository inventoryRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("")
    public String HELLO() {
        return "librarian/librarian";
    }

    @GetMapping("/authors")
    public String listAuthors(Model model) {
        List<Author> authors = authorRepository.findAll();
        model.addAttribute("authors", authors);
        return "librarian/authors/authors";
    }

    @GetMapping("/authors/new")
    public String newAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "librarian/authors/authorForm";
    }

    @PostMapping("/authors")
    public String saveAuthor(@ModelAttribute Author author) {
        authorRepository.save(author);
        return "redirect:/librarian/authors";
    }

    @GetMapping("/authors/{id}/edit")
    public String editAuthorForm(@PathVariable Long id, Model model) {
        Author author = authorRepository.findById(id).orElseThrow();
        model.addAttribute("author", author);
        return "librarian/authors/authorForm";
    }

    @PostMapping("/authors/{id}")
    public String updateAuthor(@PathVariable Long id, @ModelAttribute Author author) {
        Author existingAuthor = authorRepository.findById(id).orElseThrow();

        existingAuthor.setName(author.getName());
        existingAuthor.setYearsOfLife(author.getYearsOfLife());

        authorRepository.save(existingAuthor);
        return "redirect:/librarian/authors";
    }


    @GetMapping("/authors/{id}/delete")
    public String deleteAuthor(@PathVariable Long id) {
        authorRepository.deleteById(id);
        return "redirect:/librarian/authors";
    }



    @GetMapping("/books")
    public String listBooks(Model model) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "librarian/books/books";
    }

    @GetMapping("/books/new")
    public String newBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("publishers", publisherRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "librarian/books/bookForm";
    }

    @PostMapping("/books")
    public String saveBook(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/librarian/books";
    }


    @GetMapping("/books/{id}/edit")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id).orElseThrow();
        model.addAttribute("book", book);
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("publishers", publisherRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "librarian/books/bookForm";
    }

    @PostMapping("/books/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book) {
        Book existingBook = bookRepository.findById(id).orElseThrow();
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPublisher(book.getPublisher()); // Убедитесь, что это поле установлено
        existingBook.setCategories(book.getCategories());
        bookRepository.save(existingBook);
        return "redirect:/librarian/books";
    }

    @GetMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/librarian/books";
    }



    @GetMapping("/categories")
    public String listCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "librarian/categories/categories";
    }

    @GetMapping("/categories/new")
    public String newCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "librarian/categories/categoryForm";
    }

    @PostMapping("/categories")
    public String saveCategory(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/librarian/categories";
    }

    @GetMapping("/categories/{id}/edit")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id).orElseThrow();
        model.addAttribute("category", category);
        return "librarian/categories/categoryForm";
    }

    @PostMapping("/categories/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute Category category) {
        category.setId(id);
        categoryRepository.save(category);
        return "redirect:/librarian/categories";
    }

    @GetMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/librarian/categories";
    }




    @GetMapping("/publishers")
    public String listPublishers(Model model) {
        List<Publisher> publishers = publisherRepository.findAll();
        model.addAttribute("publishers", publishers);
        return "librarian/publishers/publishers";
    }

    @GetMapping("/publishers/new")
    public String newPublisherForm(Model model) {
        model.addAttribute("publisher", new Publisher());
        return "librarian/publishers/publisherForm";
    }

    @PostMapping("/publishers")
    public String savePublisher(@ModelAttribute Publisher publisher) {
        publisherRepository.save(publisher);
        return "redirect:/librarian/publishers";
    }

    @GetMapping("/publishers/{id}/edit")
    public String editPublisherForm(@PathVariable Long id, Model model) {
        Publisher publisher = publisherRepository.findById(id).orElseThrow();
        model.addAttribute("publisher", publisher);
        return "librarian/publishers/publisherForm";
    }

    @PostMapping("/publishers/{id}")
    public String updatePublisher(@PathVariable Long id, @ModelAttribute Publisher publisher) {
        Publisher existingPublisher = publisherRepository.findById(id).orElseThrow();
        existingPublisher.setName(publisher.getName());
        publisherRepository.save(existingPublisher);
        return "redirect:/librarian/publishers";
    }

    @GetMapping("/publishers/{id}/delete")
    public String deletePublisher(@PathVariable Long id) {
        publisherRepository.deleteById(id);
        return "redirect:/librarian/publishers";
    }




    @GetMapping("/inventories")
    public String listInventories(Model model) {
        model.addAttribute("inventories", inventoryRepository.findAll());
        return "librarian/inventories/inventories";
    }

    @GetMapping("/inventories/new")
    public String newInventoryForm(Model model) {
        model.addAttribute("inventory", new Inventory());
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("statuses", InventoryStatus.values());
        return "librarian/inventories/inventoryForm";
    }

    @PostMapping("/inventories")
    public String saveInventory(@ModelAttribute Inventory inventory) {
        inventoryRepository.save(inventory);
        return "redirect:/librarian/inventories";
    }

    @GetMapping("/inventories/{id}/edit")
    public String editInventoryForm(@PathVariable Long id, Model model) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow();
        model.addAttribute("inventory", inventory);
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("statuses", InventoryStatus.values());
        return "librarian/inventories/inventoryForm";
    }

    @PostMapping("/inventories/{id}")
    public String updateInventory(@PathVariable Long id, @ModelAttribute Inventory updatedInventory) {
        Inventory existingInventory = inventoryRepository.findById(id).orElseThrow();
        existingInventory.setBook(updatedInventory.getBook());
        existingInventory.setLocation(updatedInventory.getLocation());
        existingInventory.setStatus(updatedInventory.getStatus());
        inventoryRepository.save(existingInventory);
        return "redirect:/librarian/inventories";
    }

    @GetMapping("/inventories/{id}/delete")
    public String deleteInventory(@PathVariable Long id) {
        inventoryRepository.deleteById(id);
        return "redirect:/librarian/inventories";
    }




    @GetMapping("/requests")
    public String listRequests(Model model) {
        List<BookRequest> requests = bookRequestRepository.findAll();
        model.addAttribute("requests", requests);
        return "librarian/requests/requests";
    }

    @GetMapping("/requests/new")
    public String newRequestForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("status", RequestStatus.values());
        model.addAttribute("requestDate", LocalDate.now());
        model.addAttribute("request", new BookRequest());
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "librarian/requests/requestForm";
    }

    @PostMapping("/requests")
    public String saveRequest(@ModelAttribute BookRequest request) {
        if (request.getStatus() == null) { // Если статус не был установлен в форме
            request.setStatus(RequestStatus.PENDING);
        }
        request.setRequestDate(LocalDate.now());
        bookRequestRepository.save(request);
        return "redirect:/librarian/requests";
    }


    @GetMapping("/requests/{id}/edit")
    public String editRequestForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("status", RequestStatus.values());
        model.addAttribute("requestDate", LocalDate.now());
        BookRequest request = bookRequestRepository.findById(id).orElseThrow();
        model.addAttribute("request", request);
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "librarian/requests/requestForm";
    }

    @PostMapping("/requests/{id}")
    public String updateRequest(@PathVariable Long id, @ModelAttribute BookRequest request) {
        BookRequest existingRequest = bookRequestRepository.findById(id).orElseThrow();

        existingRequest.setUser(request.getUser());
        existingRequest.setBook(request.getBook());
        existingRequest.setStatus(request.getStatus());
        existingRequest.setRequestDate(request.getRequestDate());

        bookRequestRepository.save(existingRequest);
        return "redirect:/librarian/requests";
    }

    @GetMapping("/requests/{id}/delete")
    public String deleteRequest(@PathVariable Long id) {
        bookRequestRepository.deleteById(id);
        return "redirect:/librarian/requests";
    }
}
