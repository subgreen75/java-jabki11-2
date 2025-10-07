package model;

import service.LibraryUtils;

import java.time.LocalDate;

public class Loan {
    private int bookId;
    private int userId;
    private LocalDate loanDate;
    private LocalDate returnDate = null;

    public Loan(int bookId, int userId, LocalDate loanDate) {
        this.bookId = bookId;
        this.userId = userId;
        this.loanDate = loanDate;
    }

    public int getBookId() {
        return this.bookId;
    }

    public int getUserId() {
        return this.userId;
    }

    public LocalDate getLoanDate() {
        return this.loanDate;
    }

    public LocalDate getReturnDate() {
        return this.returnDate;
    }

    public void displayLoan() {
        System.out.printf("Читатель: %s(ID %s); Книга: %s(ID %s); Дата выдачи: %s; Дата возврата: %s\n",
                LibraryUtils.getUserNameById(this.getUserId()),
                this.getUserId(),
                LibraryUtils.getBookTitleById(this.getBookId()),
                this.getBookId(),
                this.getLoanDate(),
                this.getReturnDate());
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}