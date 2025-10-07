package model;

import service.Library;
import service.LibraryUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class User {
    private final int id;
    private String name;
    private String email;
    private static int startId = 1;

    private List<Loan> currentLoans = new ArrayList<>();

    public User(String name, String email) {
        if (name == null || name.isEmpty() || email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Некорректное значение атрибутов");
        }
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Некорректное значение email");
        }
        this.id = this.nextId();
        this.name = name;
        this.email = email;
    }

    private int nextId() {
        return startId++;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public int getId() {
        return this.id;
    }

    public void displayUser() {
        System.out.printf("ID: %d, Читатель: %s, Адрес: %s\n", this.getId(), this.getName(), this.getEmail());
    }

    private boolean validateEmail(String email) {
        return Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
                .matcher(email)
                .matches();
    }

    public List<?> getCurrentLoans() {
        return this.currentLoans;
    }

    public void addLoan(Loan loan) {
        if (this.currentLoans.size() >= 3) {
            throw new IllegalArgumentException("Выдать больше 3 книг нельзя");
        }
        //добавляем в текущие выдачи по читателю
        this.currentLoans.add(loan);
        //добавляем в историю выдачи
        Library.loans.add(loan);
        //уменьшаем кол-во доступных
        Book book = Library.books.get(loan.getBookId());
        book.setAvailableCopies(book.getAvailableCopies() - 1);
    }

    public void displayLoan() {
        System.out.println("Выданные книги:");
        for (Loan loan : this.currentLoans) {
            loan.displayLoan();
        }
    }

    public boolean checkBookOnUser(int bookId) {
        for (Loan loan : this.currentLoans) {
            if (loan.getBookId() == bookId) {
                return true;
            }
        }
        return false;
    }

    public void removeLoanByBookId(int bookId) {
        for (Loan loan : this.currentLoans) {
            if (loan.getBookId() == bookId) {
                //удалим в текущих выдачах
                this.currentLoans.remove(loan);
                //в истории заведем дату выдачи
                for (Loan loan_hist : Library.loans) {
                    if (loan_hist.getBookId() == bookId && loan_hist.getReturnDate() == null) {
                        loan_hist.setReturnDate(LocalDate.now());
                        // если у читателя несколько одинаковых книг на руках, то проставляем дату выдачи у первой попавшейся
                        break;
                    }
                }
                // если у читателя несколько одинаковых книг на руках, то удаляем первую попавшуюся. поэтому при первом нахождени выходим из цикла
                break;
            }
        }
    }
}