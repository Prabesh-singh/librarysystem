package com.library.controller;

import com.library.model.Student;
import com.library.model.Book;
import com.library.repository.StudentRepository;
import com.library.repository.BookRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String addStudent(@RequestBody Student student) {

        studentRepository.addStudent(student);

        return "Student added successfully!";
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
    public String borrowBook(
            @PathVariable String studentId,
            @PathVariable String bookId) {

        Student student = studentRepository.findStudentById(studentId);

        if (student == null) {
            return "Student not found!";
        }

        Book book = bookRepository.findBookById(bookId);

        if (book == null) {
            return "Book not found!";
        }

        if (!book.isAvailable()) {
            return "Book is already borrowed!";
        }

        // Add book to student's bookIds
        studentRepository.addBookToStudent(
                studentId,
                bookId);

        // Make book unavailable
        bookRepository.updateAvailability(
                bookId,
                false);

        return "Book borrowed successfully!";
    }

    // Return book
    @PostMapping("/{studentId}/return/{bookId}")
    public String returnBook(
            @PathVariable String studentId,
            @PathVariable String bookId) {

        Student student =
                studentRepository.findStudentById(studentId);

        if (student == null) {
            return "Student not found!";
        }

        if (!student.getBookIds().contains(bookId)) {
            return "This student has not borrowed this book!";
        }

        // Remove book from student's bookIds
        studentRepository.removeBookFromStudent(
                studentId,
                bookId
        );

        // Make book available
        bookRepository.updateAvailability(
                bookId,
                true
        );

        return "Book returned successfully!";
    }
}
