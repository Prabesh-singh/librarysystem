package com.library.repository;

import com.library.config.MongoDBConnection;
import com.library.model.Student;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    private final MongoCollection<Document> studentsCollection;

    public StudentRepository() {

        MongoDatabase database = MongoDBConnection.getDatabase();

        studentsCollection = database.getCollection("students");
    }

    // Add student
    public void addStudent(Student student) {

        Document document = new Document()
                .append("studentId", student.getStudentId())
                .append("name", student.getName())
                .append("email", student.getEmail())
                .append("bookIds", student.getBookIds());

        studentsCollection.insertOne(document);
    }

    // Get all students
    public List<Student> getAllStudents() {

        List<Student> students = new ArrayList<>();

        for (Document document : studentsCollection.find()) {

            Student student = new Student(
                    document.getString("studentId"),
                    document.getString("name"),
                    document.getString("email"));

            List<String> bookIds = document.getList("bookIds", String.class);

            if (bookIds != null) {
                student.setBookIds(bookIds);
            }

            students.add(student);
        }

        return students;
    }

    // Find student by ID
    public Student findStudentById(String studentId) {

        Document document = studentsCollection.find(
                new Document("studentId", studentId)).first();

        if (document == null) {
            return null;
        }

        Student student = new Student(
                document.getString("studentId"),
                document.getString("name"),
                document.getString("email"));

        List<String> bookIds = document.getList("bookIds", String.class);

        if (bookIds != null) {
            student.setBookIds(bookIds);
        }

        return student;
    }

    // Add borrowed book to student
    public void addBookToStudent(String studentId, String bookId) {

        studentsCollection.updateOne(
                new Document("studentId", studentId),
                new Document("$addToSet", new Document("bookIds", bookId)));
    }

    // Remove returned book from student
    public void removeBookFromStudent(String studentId, String bookId) {

        studentsCollection.updateOne(
                new Document("studentId", studentId),
                new Document("$pull", new Document("bookIds", bookId))
        );
    }
}
