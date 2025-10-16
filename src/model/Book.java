package model;

public class Book {
    private final int id;
    private String title;
    private String author;
    private int year;
    private int totalCopies;
    private int availableCopies;
    //стартовое значение для bookID
    private static int startId = 1;


    public Book(String title, String author, int year, int totalCopies) {
        validateBook(title, author, year, totalCopies);
        this.title = title;
        this.author = author;
        this.year = year;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.id = this.nextId();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public int getYear() {
        return this.year;
    }

    public int getTotalCopies() {
        return this.totalCopies;
    }

    private int nextId() {
        return startId++;
    }

    public int getAvailableCopies() {
        return this.availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    private void validateBook(String title, String author, int year, int totalCopies) {
        if (title == null || author == null || title.isBlank() || author.isBlank() || year < 0 || totalCopies < 0) {
            throw new IllegalArgumentException("Некорректное значение атрибутов книги");
        }
    }
    /**
     *выводит в консоль информацию о книге
     */
    public void displayBook() {
        System.out.printf("ID: %d, Название: %s, Автор: %s, Год издания: %d, Всего копий: %d, В наличии %d\n", this.getId(), this.getTitle(), this.getAuthor(), this.getYear(), this.getTotalCopies(), this.getAvailableCopies());
    }
}