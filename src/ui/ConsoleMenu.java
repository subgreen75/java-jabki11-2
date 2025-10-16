package ui;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;

import model.Book;
import model.User;
import service.LibraryUtils;

import static service.Library.books;
import static service.Library.users;

// Метод Консольное Меню
public class ConsoleMenu {
    private static final Scanner scanner = new Scanner(System.in);
    public static void start() {
        int choice = -1;
        while (choice != 0) {
            displayMenu();
            System.out.print("Введите ваш выбор: ");
            choice = scanner.nextInt();
            processChoice(choice, scanner);
        }
        System.out.println("Программа завершена.");
        scanner.close();
    }

    // Метод для отображения пунктов меню
    public static void displayMenu() {
        System.out.println("\n--- Консольное меню ---");
        System.out.println("1. Добавить книгу");
        System.out.println("2. Добавить читателя");
        System.out.println("3. Просмотр всех книг");
        System.out.println("4. Просмотр всех читателей");
        System.out.println("5. Поиск книг по: названию, автору, году");
        System.out.println("6. Поиск пользователя по ID");
        System.out.println("7. Выдача книги");
        System.out.println("8. Возврат книги");
        System.out.println("9. Просмотр всех выданных книг");
        System.out.println("10. Просмотр  выданных книг по пользователю");
        System.out.println("11. Просмотр  выданных книг по конкретной книге");
        System.out.println("12. Поиск просроченных выдач");
        System.out.println("0. Выход");
        System.out.println("-----------------------");
    }

    // Метод для обработки выбора пользователя
    public static void processChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                System.out.println("Вы выбрали Пункт 1. Добавить книгу");
                processBook();
                break;
            case 2:
                System.out.println("Вы выбрали Пункт 2. Добавить читателя");
                processUser();
                break;
            case 3:
                System.out.println("Вы выбрали Пункт 3. Просмотр всех книг");
                LibraryUtils.displayBooks(books);
                break;
            case 4:
                System.out.println("Вы выбрали Пункт 4. Просмотр всех пользователей");
                LibraryUtils.displayUsers(users);
                break;
            case 5:
                System.out.println("Вы выбрали Пункт 5. Поиск книг по: названию, автору, году");
                processFindBook();
                break;
            case 6:
                System.out.println("Вы выбрали Пункт 6. Поиск пользователя по ID");
                processFindUser();
                break;
            case 7:
                System.out.println("Вы выбрали Пункт 7. Выдача книги");
                processLendingBook();
                break;
            case 8:
                System.out.println("Вы выбрали Пункт 8. Возврат книги");
                processReturnBook();
                break;
            case 9:
                System.out.println("Вы выбрали Пункт 9. История выдачи книг");
                LibraryUtils.displayAllLoans();
                break;
            case 10:
                System.out.println("Вы выбрали Пункт 10. Просмотр  выданных книг по пользователю");
                processFindLoanByUserId();
                break;
            case 11:
                System.out.println("Вы выбрали Пункт 11. Просмотр  выданных книг по конкретной книге");
                processFindLoanByBookId();
                break;
            case 12:
                System.out.println("Вы выбрали Пункт 12. Поиск просроченных выдач");
                processFindLoanByDaysOverdue();
                break;
            case 0:
                break;
            default:
                System.out.println("Неверный ввод. Пожалуйста, выберите один из пунктов меню.");
        }
    }

    public static void processBook() {
        String title, author;
        int year, totalCopies;
        try {
            title = input("введите название книги:");
            author = input("введите ФИО автора:");
            year = Integer.parseInt(input("введите год издания:"));
            totalCopies = Integer.parseInt(input("введите количество копий:"));
            LibraryUtils.addBook(title, author, year, totalCopies, true);
            System.out.println("Книга добавлена");
        } catch (NumberFormatException e) {
            System.out.println("Не числовые значения года издания или количество копий");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // добавляет читателя в мап users
    public static void processUser() {
        String name, email;
        try {
            name = input("введите ФИО читателя:");
            email = input("введите адрес эл.почты читателя:");
            LibraryUtils.addUser(name, email, true);
            System.out.println("Читатель добавлен");
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    //вводим значения названия книги, автора, год издания и ищет книгу по комбинации введенных значений
    public static void processFindBook() {
        String title, author;
        int year;
        HashMap<Integer, Book> booksFind;
        System.out.println("Подсказка: поиск может производится по одному или нескольким параметрам. Если поиск по параметру не нужен, просто нажмите ENTER");
        try {
            title = input("введите название книги (пропустить - нажмите ENTER):");
            author = input("введите ФИО автора (пропустить - нажмите ENTER):");
            try {
                year = Integer.parseInt(input("введите год издания(пропустить - нажмите ENTER):"));
            } catch (Exception e) {
                year = 0;
            }
            if ((title == null || title.isBlank()) && (author == null || author.isBlank()) && year == 0) {
                throw new Exception("Не введено ни одного параметра поиска. Уточните хотя бы один параметр поиска");
            }
            booksFind = LibraryUtils.getBooks(0, title, author, year);
            LibraryUtils.displayBooks(booksFind);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //вводим значения ID читателя и ищет читателя по введенному значению ID
    public static void processFindUser() {
        int id;
        HashMap<Integer, User> usersFind;
        try {
            try {
                id = Integer.parseInt(input("введите ID читателя:"));
            } catch (NumberFormatException e) {
                id = 0;
            }
            if (id == 0) {
                throw new Exception("Не введено значение id читателя. Поиск прекращен");
            }
            usersFind = LibraryUtils.getUsers(id, null, null);
            LibraryUtils.displayUsers(usersFind);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
      вводим название книги, автора, год издания и ищет книгу по комбинации введенных значений
      вводим значение ID читателя
      запускаем метод  Library.lendingBook для выдачи книги
     */
    public static void processLendingBook() {
        String title, author;
        int year, userID, lendingCopies = 1;
        HashMap<Integer, Book> booksFind;
        try {
            try {
                userID = Integer.parseInt(input("введите ID читателя:"));
            } catch (NumberFormatException e) {
                throw new Exception("Введен некорректный id читателя. Поиск прекращен");
            }
            System.out.println("Подсказка: поиск книги может производится по одному или нескольким параметрам. Если поиск по параметру не нужен, просто нажмите ENTER");
            title = input("введите название книги (пропустить - нажмите ENTER):");
            author = input("введите ФИО автора (пропустить - нажмите ENTER):");
            try {
                year = Integer.parseInt(input("введите год издания (пропустить - нажмите ENTER):"));
            } catch (NumberFormatException e) {
                year = 0;
            }
            if ((title == null || title.isBlank()) && (author == null || author.isBlank()) && year == 0) {
                throw new Exception("Не введено ни одного параметра поиска. Уточните хотя бы один параметр поиска");
            }
            booksFind = LibraryUtils.getBooks(0, title, author, year);
            if (booksFind.size() == 0 || booksFind.size() > 1) {
                System.out.println("По вашему запросу не найдено книг или найдено более одной. Уточните параметры поиска");
                LibraryUtils.displayBooks(booksFind);
                return;
            }
            for (Book book : booksFind.values()) {
                LibraryUtils.loanBookToUser(userID, book.getId(), LocalDate.now());
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
      вводим значение ID  книги
      вводим значение ID читателя
      запускаем метод  Library.returnBook для возврата книги
     */
    public static void processReturnBook() {
        int bookID, userID;
        try {
            userID = Integer.parseInt(input("введите ID читателя:"));
            bookID = Integer.parseInt(input("введите ID книги:"));
            LibraryUtils.returnBook(userID, bookID);
        } catch (NumberFormatException e) {
            System.out.println("Введите ID читателя и ID книги. Поиск прекращен");
        }
    }

    // метод для ввода в консоли
    public static String input(String prompt) {
        System.out.print(prompt);
        Scanner scannerIn = new Scanner(System.in);
        return scannerIn.nextLine();
    }

    //вводим значения ID читателя и ищет выданные книги
    public static void processFindLoanByUserId() {
        int id;
        try {
            try {
                id = Integer.parseInt(input("введите ID читателя:"));
            } catch (NumberFormatException e) {
                id = 0;
            }
            if (id == 0) {
                throw new Exception("Не введено значение id читателя. Поиск прекращен");
            }
            LibraryUtils.displayAllLoansByUserID(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //вводим значения ID читателя и ищет выданные книги
    public static void processFindLoanByBookId() {
        int id;
        try {
            try {
                id = Integer.parseInt(input("введите ID книги:"));
            } catch (NumberFormatException e) {
                id = 0;
            }
            if (id == 0) {
                throw new Exception("Не введено значение id книги. Поиск прекращен");
            }
            LibraryUtils.displayAllLoansByBookID(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //вводим количество дней просрочки и ищем выданные книги
    public static void processFindLoanByDaysOverdue() {
        int daysOverdue;
        try {
            try {
                daysOverdue = Integer.parseInt(input("Количестро дней просрочки:"));
            } catch (NumberFormatException e) {
                daysOverdue = -1;
            }
            if (daysOverdue == -1) {
                throw new Exception("Не введено значение Количество дней просрочки. Поиск прекращен");
            }
            LibraryUtils.displayAllLoansOverdue(daysOverdue);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}