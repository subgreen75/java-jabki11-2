import service.Library;
import ui.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        Library.init();
        ConsoleMenu.start();
    }
}