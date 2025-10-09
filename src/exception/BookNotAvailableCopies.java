package exception;

public class BookNotAvailableCopies extends Exception {
    public BookNotAvailableCopies(String title, int copies) {
        super("Книги " + title + " нет в наличии, доступно " + copies + " экземпляров");
    }


}