package com.library.controller;

import com.library.model.Book;
import com.library.repository.BookRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    // Constructor
    public BookController() {
        this.bookRepository = new BookRepository();
    }

    // GET all books
    // GET http://localhost:8080/api/books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    // GET book by ID
    // GET http://localhost:8080/api/books/B001
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable String id) {
        return bookRepository.findBookById(id);
    }

    // ADD book
    // POST http://localhost:8080/api/books
    @PostMapping
    public String addBook(@RequestBody Book book) {
        bookRepository.addBook(book);
        return "Book added successfully!";
    }

    // DELETE book
    // DELETE http://localhost:8080/api/books/B001
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable String id) {
        bookRepository.deleteBook(id);
        return "Book deleted successfully!";
    }

    // UPDATE availability
    // PUT http://localhost:8080/api/books/B001/availability?available=false
    @PutMapping("/{id}/availability")
    public String updateAvailability(
            @PathVariable String id,
            @RequestParam boolean available) {

        bookRepository.updateAvailability(id, available);

        return "Book availability updated!";
    }
}
