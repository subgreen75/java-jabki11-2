import exception.BookNotAvailableCopies;
import exception.UserNotFoundByID;
import model.Book;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.Library;
import service.LibraryUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class MainTest {
    @Test
        // проверка добавление книги
    void testAddBook() {
        LibraryUtils.addBook("Какая то книга", "Какой то автор", 2005, 1, false);
        LibraryUtils.displayBooks(Library.books);
        // добавим с пустым названием
        Assertions.assertThrows(IllegalArgumentException.class, () -> LibraryUtils.addBook("", "Какой то автор", 2005, 1, false));
        Assertions.assertThrows(IllegalArgumentException.class, () -> LibraryUtils.addBook("Книга 2", "", 2005, 1, false));

    }

    //проверка добавления читателя
    @Test
    void testAddUser() {
        LibraryUtils.addUser("Читатель1","1@1.ru", false);
        // добавим с пустым названием
        Assertions.assertThrows(IllegalArgumentException.class, () -> LibraryUtils.addUser("","1@1.ru", false));
        Assertions.assertThrows(IllegalArgumentException.class, () -> LibraryUtils.addUser("Какой то читатель","", false));
        LibraryUtils.displayUsers(Library.users);
    }

    //проверка поиска книги
    @Test
    void testFindBook() {
        HashMap<Integer, Book> findBooks;
        System.out.println("Весь список книг:");
        LibraryUtils.displayBooks(Library.books);

        System.out.println("проверка на \"НЕ НАШЛОСЬ\":");
        //проверка на "НЕ НАШЛОСЬ"
        findBooks = LibraryUtils.getBooks(0, "НАЗВАНИЕ", "АВТОР", 1991);
        LibraryUtils.displayBooks(findBooks);
        Set<Integer> expectedBooks = new HashSet<>();
        Assertions.assertArrayEquals(expectedBooks.toArray(), findBooks.keySet().toArray());

        System.out.println("проверка на \"НАШЛОСЬ\":");
        //проверка на "НАШЛОСЬ" сравниваем список id
        expectedBooks.add(1);
        findBooks = LibraryUtils.getBooks(0, "КНИГА", "АВТОР", 2005);
        LibraryUtils.displayBooks(findBooks);
        Assertions.assertArrayEquals(expectedBooks.toArray(), findBooks.keySet().toArray());
    }

    //проверка поиска читателя
    @Test
    void testFindUser()  {
        HashMap<Integer, User> usersFind;
        Set<Integer> expectedUsers = new HashSet<>();
        System.out.println("Весь список читателей:");
        LibraryUtils.displayUsers(Library.users);

        System.out.println("");
        System.out.println("Ищем по пустым параметрам (по идее должен найти все)");
        usersFind = LibraryUtils.getUsers(0, null, null);
        expectedUsers.add(1);
        Assertions.assertArrayEquals(expectedUsers.toArray(), usersFind.keySet().toArray());
        LibraryUtils.displayUsers(usersFind);

        System.out.println("");
        System.out.println("Ищем по ID = 1");
        usersFind = LibraryUtils.getUsers(1, null, null);
        expectedUsers.clear();
        expectedUsers.add(1);
        Assertions.assertArrayEquals(expectedUsers.toArray(), usersFind.keySet().toArray());
        LibraryUtils.displayUsers(usersFind);

        System.out.println("");
        System.out.println("Ищем по ФИО =  Читатель1");
        usersFind = LibraryUtils.getUsers(0, "Читатель1", null);
        expectedUsers.clear();
        expectedUsers.add(1);
        Assertions.assertArrayEquals(expectedUsers.toArray(), usersFind.keySet().toArray());
        LibraryUtils.displayUsers(usersFind);

        System.out.println("");
        System.out.println("Ищем по ID = 1 и ФИО =  Читатель1");
        usersFind = LibraryUtils.getUsers(1, "Читатель1", null);
        expectedUsers.clear();
        expectedUsers.add(1);
        Assertions.assertArrayEquals(expectedUsers.toArray(), usersFind.keySet().toArray());
        LibraryUtils.displayUsers(usersFind);

        System.out.println("");
        System.out.println("Ищем по ФИО =  Читатель2 (найтись не должно)");
        usersFind = LibraryUtils.getUsers(0, "Читатель2", null);
        expectedUsers.clear();
        Assertions.assertArrayEquals(expectedUsers.toArray(), usersFind.keySet().toArray());
        LibraryUtils.displayUsers(usersFind);

        System.out.println("");
        System.out.println("Ищем по емейл like ru");
        usersFind = LibraryUtils.getUsers(0, "", "ru");
        expectedUsers.clear();
        expectedUsers.add(1);
        Assertions.assertArrayEquals(expectedUsers.toArray(), usersFind.keySet().toArray());
        LibraryUtils.displayUsers(usersFind);
    }

    //проверка выдачи и возврата книг
    @Test
    void testLendingBook()  {
        LibraryUtils.displayBooks(Library.books);
        System.out.println("выдали книгу 1");
        LibraryUtils.loanBookToUser(1, 1,  LocalDate.now());

        //сравним доступное
        Assertions.assertEquals(Library.books.get(1).getAvailableCopies(), 0);
        System.out.println("еще раз выдали книгу 1");
        LibraryUtils.loanBookToUser(1, 1, LocalDate.now());
        LibraryUtils.loanBookToUser(1, 1, LocalDate.now());
        //проверим вывод выданных книг
        LibraryUtils.displayAllLoans();
        System.out.println("вернули книгу 1");
        LibraryUtils.returnBook(1,1);
        //сравним доступное
        Assertions.assertEquals(Library.books.get(1).getAvailableCopies(), 1);
        LibraryUtils.displayAllLoans();
        LibraryUtils.displayBooks(Library.books);
        System.out.println("вернули книгу 1");
        LibraryUtils.returnBook(1,1);
        //сравним доступное
        Assertions.assertEquals(Library.books.get(1).getAvailableCopies(), 1);
        System.out.println("вернули книгу 2 по читателю 2");
        LibraryUtils.returnBook(2,2);

    }
}