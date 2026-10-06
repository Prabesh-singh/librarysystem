package com.library.repository;

import com.library.config.MongoDBConnection;
import com.library.model.Book;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    private final MongoCollection<Document> booksCollection;

    // Constructor
    public BookRepository() {

        MongoDatabase database = MongoDBConnection.getDatabase();

        booksCollection = database.getCollection("books");
    }

    // Add a book
    public void addBook(Book book) {

        Document document = new Document()
                .append("id", book.getId())
                .append("title", book.getTitle())
                .append("author", book.getAuthor())
                .append("category", book.getCategory())
                .append("available", book.isAvailable());

        booksCollection.insertOne(document);

        System.out.println("Book added successfully!");
    }

    // Get all books
    public List<Book> getAllBooks() {

        List<Book> books = new ArrayList<>();

        for (Document document : booksCollection.find()) {

            Book book = new Book(
                    document.getString("id"),
                    document.getString("title"),
                    document.getString("author"),
                    document.getString("category"),
                    document.getBoolean("available"));

            books.add(book);
        }

        return books;
    }

    // Find book by ID
    public Book findBookById(String id) {

        Document document = booksCollection.find(
                new Document("id", id)).first();

        if (document == null) {
            return null;
        }

        return new Book(
                document.getString("id"),
                document.getString("title"),
                document.getString("author"),
                document.getString("category"),
                document.getBoolean("available"));
    }

    // Delete book
    public void deleteBook(String id) {

        booksCollection.deleteOne(
                new Document("id", id));

        System.out.println("Book deleted successfully!");
    }

    // Update book availability
    public void updateAvailability(String id, boolean available) {

        booksCollection.updateOne(
                new Document("id", id),
                new Document("$set",
                        new Document("available", available)));

        System.out.println("Book availability updated!");
    }
}
