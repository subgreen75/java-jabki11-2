package exception;

public class UserNotFoundByID extends Exception {
    public UserNotFoundByID(int userID) {
        super("Не найден читатель по ID " + userID);
    }
}