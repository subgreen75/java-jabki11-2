package service;

import exception.BookNotAvailableCopies;
import exception.BookNotFoundByID;
import exception.LendingNotFoundByUserID;
import exception.UserNotFoundByID;
import model.Book;
import model.Loan;
import model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;


public class LibraryUtils {
    /**
      метод добавляет книгу в мап books
      saveToFileFlag - записывать или нет в файл новую книгу. по умолчанию (при загрузке из файле не перезаписываем)
    */
    public static void addBook(String title, String author, int year, int totalCopies, boolean saveToFileFlag) {
        Book book = new Book(title, author, year, totalCopies);
        HashMap<Integer, Book> findBooks;
        findBooks = LibraryUtils.getBooks(0, title, author, year);
        if (findBooks.isEmpty()) {
            // если не нашли - то добавляем
            Library.books.put(book.getId(), book);
            if (saveToFileFlag) {
                saveBookToFile(book);
            }
        }
        //если нашли только одну книгу
        if (findBooks.size() == 1) {
            for (Book bookFind : findBooks.values()) {
                //увеличиваем общее количество и доступное
                bookFind.setTotalCopies(bookFind.getTotalCopies() + totalCopies);
                bookFind.setAvailableCopies(bookFind.getAvailableCopies() + totalCopies);
                //обновляем мап
                Library.books.put(bookFind.getId(), bookFind);
            }
        }
        //если нашли более одной книги - сообщаем. ничего не делаем
        if (findBooks.size() > 1) {
            System.out.printf("В картотеке найдено бюолее одной книги %s, автора %s и годом издания %d. Книга не добавлена. Привидите порядок картотеку", title, author, year);
        }

    }

    /**
     * метод добавляет книгу в мап users
     * saveToFileFlag - записывать или нет в файл новую книгу. по умолчанию (при загрузке из файле не перезаписываем)
     */
    public static void addUser(String name, String email, boolean saveToFileFlag) {
        User user = new User(name, email);
        Library.users.put(user.getId(), user);
        if (saveToFileFlag) {
            saveUserToFile(user);
        }
    }

    // выводит в консоль списки книг в параметре мапе books
    public static void displayBooks(Map<Integer, Book> books) {
        System.out.printf("Найдено книг: %d\n", books.size());
        System.out.println("Список:");
        for (Map.Entry<Integer, Book> entry : books.entrySet()) {
            entry.getValue().displayBook();
        }
    }

    // выводит в консоль списки читателей в параметре мапе users
    public static void displayUsers(Map<Integer, User> users) {
        System.out.printf("Найдено читателей : %d\n", users.size());
        System.out.println("Список:");
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            entry.getValue().displayUser();
        }
    }

    // метод - ищет по id, названию,автору, году книги и вовзращает мап типа books
    public static HashMap<Integer, Book> getBooks(int id, String title, String author, int year) {
        HashMap<Integer, Book> booksRes = new HashMap<>();
        if (id != 0 && Library.books.containsKey(id)) {
            booksRes.put(id, Library.books.get(id));
        } else {
            for (Book book : Library.books.values()) {
                if (((title != null && !title.isBlank() && book.getTitle().toUpperCase().matches(".*" + title.toUpperCase() + ".*")) || (title == null || title.isBlank())) &&
                        ((author != null && !author.isBlank() && book.getAuthor().toUpperCase().matches(".*" + author.toUpperCase() + ".*")) || (author == null || author.isBlank())) &&
                        ((year != 0 && book.getYear() == year) || year == 0)
                ) {
                    booksRes.put(book.getId(), book);
                }
            }
        }
        return booksRes;
    }

    // метод - ищет по id, ФИО, емейлу  читателей и вовзращает мап типа users
    public static HashMap<Integer, User> getUsers(int id, String name, String email) {
        HashMap<Integer, User> usersRes = new HashMap<>();
        if (id != 0 & Library.users.containsKey(id)) {
            usersRes.put(id, Library.users.get(id));
        } else {
            for (User user : Library.users.values()) {
                if (((name != null && !name.isBlank() && user.getName().toUpperCase().matches(".*" + name.toUpperCase() + ".*")) || (name == null || name.isBlank())) &&
                        ((email != null && !email.isBlank() && user.getEmail().toUpperCase().matches(".*" + email.toUpperCase() + ".*")) || (email == null || email.isBlank()))
                ) {
                    usersRes.put(user.getId(), user);
                }
            }
        }
        return usersRes;
    }

    //метод возврата книги для читателя с userID и книги с  bookID.
    public static void returnBook(int userID, int bookID) {
        try {
            //проверим есть ли  читатели и книги с такими  userID  и bookID. если нет - выходим
            if (!Library.users.containsKey(userID)) {
                throw new UserNotFoundByID(userID);
            }
            if (!Library.books.containsKey(bookID)) {
                throw new BookNotFoundByID(bookID);
            }
            //проврим, а есть ли в списке выданных книг та, которую пытаются вернуть. если нет, выходим
            Book book = Library.books.get(bookID);
            User user = Library.users.get(userID);
            if (!user.checkBookOnUser(bookID)) {
                throw new LendingNotFoundByUserID(userID);
            }
            user.removeLoanByBookId(bookID);
            //увеливаем доступное кол-во книг в мапе books
            Library.books.get(bookID).setAvailableCopies(Library.books.get(bookID).getAvailableCopies() + 1);
            System.out.printf("Книга %s, вернули\n", Library.books.get(bookID).getTitle());
        } catch (UserNotFoundByID | BookNotFoundByID | LendingNotFoundByUserID e) {
            System.out.println(e.getMessage());
        }
    }

    //записывает книгу в файл
    public static void saveBookToFile(Book book) {
        try (FileWriter writer = new FileWriter(Library.csvFileBook, true)) {
            writer.write("\n" + book.getTitle() + ";" + book.getAuthor() + ";" + book.getYear() + ";" + book.getAvailableCopies());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //записывает читателя в файл
    public static void saveUserToFile(User user) {
        try (FileWriter writer = new FileWriter(Library.csvFileUser, true)) {
            writer.write("\n" + user.getName() + ";" + user.getEmail());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //выдача книги
    public static void loanBookToUser(int userID, int bookID, LocalDate loanDate) {
        try {
            //проверим есть ли  читатели и книги с такими  userID  и bookID. если нет - выходим
            if (!Library.users.containsKey(userID)) {
                throw new UserNotFoundByID(userID);
            }
            if (!Library.books.containsKey(bookID)) {
                throw new BookNotFoundByID(bookID);
            }
            if (loanDate == null) {
                throw new IllegalArgumentException("Некорректное значение атрибутов");
            }
            User user = Library.users.get(userID);
            Book book = Library.books.get(bookID);
            if (book.getAvailableCopies() == 0) {
                throw new BookNotAvailableCopies(book.getTitle(), book.getAvailableCopies());
            }
            //добавлем в список текущих выдач
            user.addLoan(new Loan(bookID, userID, loanDate));
            System.out.printf("Книга %s  выдана \n", book.getTitle());
            user.displayLoan();

        } catch (UserNotFoundByID | BookNotFoundByID | IllegalArgumentException | BookNotAvailableCopies e) {
            System.out.println(e.getMessage());
        }
    }

    //ФИО читателя по ID
    public static String getUserNameById(int userID) {
        User user = Library.users.get(userID);
        return user.getName();
    }

    //Название книги по ID
    public static String getBookTitleById(int bookID) {
        Book book = Library.books.get(bookID);
        return book.getTitle();
    }

    //история выдачи книг
    public static void displayAllLoans() {
        System.out.println("История выдачи книг:");
        Library.loans.stream()
                .forEach(loan -> loan.displayLoan());
    }

    //история выдачи книг по пользователю
    public static void displayAllLoansByUserID(int userID) {
        System.out.printf("История выдачи книг по пользователю %s:\n", getUserNameById(userID));
        Library.loans.stream()
                .filter(loan -> loan.getUserId() == userID)
                .forEach(loan -> loan.displayLoan());
    }

    /**
     * история выдачи книг по книге
     * @param bookID - ID книги
     */
    public static void displayAllLoansByBookID(int bookID) {
        System.out.printf("История выдачи книг по книге %s:\n", getBookTitleById(bookID));
        Library.loans.stream()
                .filter(loan -> loan.getBookId() == bookID)
                .forEach(loan -> loan.displayLoan());
    }

    /**
     * история просроченных  книг
     * @param daysOverdue - число дней просрочки
     */
    public static void displayAllLoansOverdue(int daysOverdue) {
        System.out.printf("Просроченные книги за %s дн.:\n", daysOverdue);
        Library.loans.stream()
                .filter(loan -> loan.getReturnDate() == null && ChronoUnit.DAYS.between(loan.getLoanDate(), LocalDate.now())  >= daysOverdue)
                .forEach(loan -> loan.displayLoan());
    }
}
