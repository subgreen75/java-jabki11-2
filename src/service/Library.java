package service;

import model.Book;
import model.Loan;
import model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Library {
    // мап списки книги. ключ - UserID
    public static HashMap<Integer, Book> books = new HashMap<>();
    // мап списки читателей. ключ - BookID
    public static HashMap<Integer, User> users = new HashMap<>();
    // мап списки выданных книг. ключ - UserID, значения - другой мап (ключ - BookID, значение - количество книг на руках)
    //public static HashMap<Integer, HashMap<Integer, Integer>> lendingBooks = new HashMap<>();
    //файл scv книг с исходными данными
    public static String csvFileBook = "src/resources/books.csv";
    //файл scv книг с исходными данными
    public static String csvFileUser = "src/resources/users.csv";

    public static List<Loan> loans = new ArrayList<>();

    //метод инициализации начальных значений. загружаем из csv файлов src/service/books.csv и src/service/users.csv
    public static void init() {
        loadBooksFromFile();
        loadUsersFromFile();
        // для тестирования просроченной выдачи заведем несколько просроченных выдач
        LibraryUtils.loanBookToUser(1, 1, LocalDate.of(2025, 8, 1));
        LibraryUtils.loanBookToUser(2, 1, LocalDate.of(2025, 8, 25));
        LibraryUtils.loanBookToUser(1, 3, LocalDate.of(2025, 7, 5));

    }


    // метод загружает из csv файла в мап books
    private static void loadBooksFromFile() {
        books.clear();
        String line;
        String csvSplitBy = ";";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFileBook))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                LibraryUtils.addBook(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]), false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // метод загружает из csv файла в мап users
    private static void loadUsersFromFile() {
        users.clear();
        String line;
        String csvSplitBy = ";";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFileUser))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                LibraryUtils.addUser(data[0], data[1], false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}