package exception;

public class BookReturnGreaterThanAllowed extends Exception {
    public BookReturnGreaterThanAllowed() {
        super("Попытка вернуть лишние книги");
    }
}