package exception;

public class LendingNotFoundByUserID extends Exception {
    public LendingNotFoundByUserID(int UserID) {
        super("Не найдены выданные книги по читателю с ID " + UserID);
    }
}