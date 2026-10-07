package com.library.controller;

import com.library.model.Student;
import com.library.model.Book;
import com.library.repository.StudentRepository;
import com.library.repository.BookRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;

    public StudentController() {
        this.studentRepository = new StudentRepository();
        this.bookRepository = new BookRepository();
    }

    // Add student
    @PostMapping
    public ResponseEntity<Map<String, Object>> addStudent(@RequestBody Student student) {
        studentRepository.addStudent(student);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Student added successfully!");
        response.put("student", student);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    // Get student by ID
    @GetMapping("/{studentId}")
    public Student getStudent(@PathVariable String studentId) {
        return studentRepository.findStudentById(studentId);
    }

    // Borrow book
    @PostMapping("/{studentId}/borrow/{bookId}")
    public ResponseEntity<Map<String, Object>> borrowBook(
            @PathVariable String studentId,
            @PathVariable String bookId) {

        Map<String, Object> response = new HashMap<>();
        Student student = studentRepository.findStudentById(studentId);

        if (student == null) {
            response.put("success", false);
            response.put("message", "Student not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Book book = bookRepository.findBookById(bookId);

        if (book == null) {
            response.put("success", false);
            response.put("message", "Book not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (!book.isAvailable()) {
            response.put("success", false);
            response.put("message", "Book is already borrowed!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Add book to student's bookIds
        studentRepository.addBookToStudent(studentId, bookId);

        // Make book unavailable
        bookRepository.updateAvailability(bookId, false);

        response.put("success", true);
        response.put("message", "Book borrowed successfully!");
        return ResponseEntity.ok(response);
    }

    // Return book
    @PostMapping("/{studentId}/return/{bookId}")
    public ResponseEntity<Map<String, Object>> returnBook(
            @PathVariable String studentId,
            @PathVariable String bookId) {

        Map<String, Object> response = new HashMap<>();
        Student student = studentRepository.findStudentById(studentId);

        if (student == null) {
            response.put("success", false);
            response.put("message", "Student not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (!student.getBookIds().contains(bookId)) {
            response.put("success", false);
            response.put("message", "This student has not borrowed this book!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Remove book from student's bookIds
        studentRepository.removeBookFromStudent(studentId, bookId);

        // Make book available
        bookRepository.updateAvailability(bookId, true);

        response.put("success", true);
        response.put("message", "Book returned successfully!");
        return ResponseEntity.ok(response);
    }
}