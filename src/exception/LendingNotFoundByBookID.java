package exception;

public class LendingNotFoundByBookID extends Exception {
    public LendingNotFoundByBookID(int UserID, int BookID) {
        super("Не найдены выданные книги по читателю с ID " + UserID + " и книги с ID " + BookID);
    }
}