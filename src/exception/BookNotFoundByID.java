package exception;

public class BookNotFoundByID extends Exception {
    public BookNotFoundByID(int bookID) {
        super("Не найдена книга по ID " + bookID);
    }
}