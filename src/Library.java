/*
UCF COP3330 Spring 2022
Group Project

Jason Grossman
Joseph Eddy
Brian Castro
*/

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

abstract class User {
    public static final Map<String, User> map = new LinkedHashMap<>();

    private static int count = 0;
    protected int id;

    private String name;
    private int age;
    protected String username;
    protected String password;
    private final Calendar dateCreated; // date account was created

    protected User(String name, int age, String username, String password) {
        this.id = count++; // increment global count
        this.name = name;
        this.age = age;
        this.username = username;
        this.password = password;
        this.dateCreated = Calendar.getInstance();

        map.put(username, this);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public Calendar getDateCreated() { return dateCreated; }

    public boolean loginBool(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // Static methods
    public static void printAllUsers() {
        System.out.println("\n--ALL USER ACCOUNTS--");
        for (User user : User.map.values()) {
            user.printUser();
            System.out.println();
        }
        System.out.println();
    }

    public static boolean doesUsernameExist(String username) {
        return map.get(username) != null;
    }

    // User abstract methods
    public abstract void printUser();
}

class Librarian extends User {
    // public String department;

    public Librarian(String name, int age, String username, String password) {
        super(name, age, username, password);
    }

    public Student getStudents() {
        return null;
    }

    protected void enableUser(Student student) {

    }

    protected void disableUser(Student student) {

    }

    @Override
    public void printUser() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        System.out.printf("Librarian Account: %s (ID %d), Created on %s\nName: %s, Age: %d\n",
                this.getUsername(), this.getId(), formatter.format(this.getDateCreated().getTime()),
                this.getName(), this.getAge());
    }
}


class Student extends User {
    public enum Status {
        FRESHMAN, SOPHMORE, JUNIOR, SENIOR, GRADUATE;

        public static Status fromInteger(int n) {
            return switch (n) {
                case 2 -> Student.Status.SOPHMORE;
                case 3 -> Student.Status.JUNIOR;
                case 4 -> Student.Status.SENIOR;
                case 5 -> Student.Status.GRADUATE;
                default -> Student.Status.FRESHMAN;
            };
        }

        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

    private boolean enabled;
    private Status status; // year grade status of student
    private Borrowing borrow; // can only have one

    public Student(String name, int age, String username, String password) {
        this(Status.FRESHMAN, name, age, username, password);
    }

    public Student(String name, int age, String username, String password, Status status) {
        this(status, name, age, username, password);
    }

    private Student(Status status, String name, int age, String username, String password) {
        super(name, age, username, password);

        this.enabled = true; // default true
        this.status = status;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Borrowing getBorrow() {
        return borrow;
    }

    public boolean isBorrowing() { return getBorrow() != null; }

    public Borrowing createBorrowing() {
        if (isBorrowing()) {
            return null;
        }

        borrow = new Borrowing(this);
        return borrow;
    }

    public void removeBorrowing() {
        borrow = null;
    }

    // add borrowing to global transactions list
    public void checkout() {
        Borrowing.transactions.add(getBorrow());
    }

    public void returnBooks() {
        borrow.setReturned(false);
        borrow = null;
    }

    @Override
    public void printUser() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        System.out.printf("Student Account: %s (ID %d), Created on %s\nName: %s, Age: %d\nStatus: %s, Account Enabled: %s\n",
                this.getUsername(), this.getId(), formatter.format(this.getDateCreated().getTime()),
                this.getName(), this.getAge(),
                this.getStatus().toString(), this.isEnabled());
    }
}

class Borrowing {
    public static final List<Borrowing> transactions = new ArrayList<>();
    private static final int maxBooks = 3;
    private static int count = 0;
    public int id;

    public List<Book> books = new LinkedList<>(); // 0..3 books
    public Student borrower;
    public Calendar dateBorrowed;
    public Calendar dateDue;
    private Calendar dateReturned;
    private Boolean returned;

    public Borrowing(Student borrower){
        this.id = count++; // increment class global count

        this.dateBorrowed = Calendar.getInstance();
        this.dateDue = Calendar.getInstance();
        this.dateDue.add(Calendar.DAY_OF_YEAR, 30); // set due date to 30 days from today

        this.borrower = borrower;
        this.returned = false;
    }

    public void printBorrowedBooks() {
        for (Book book : books) {
            if (book != null) book.printBook();
        }
    }

    public void printBorrowedBookTitles() {
        int i = 1;
        for (Book book : books) {
            if (book != null) {
                System.out.printf("Book %d: \"%s\"\n", i, book.getTitle());
            }

            i++;
        }
    }

    public void printBorrowing() {
        System.out.printf("==Transaction %d==\n--User Info--\n", id);
        borrower.printUser();
        System.out.println("--Borrowed Books--");
        printBorrowedBooks();
        System.out.println();
    }

    public static void printAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("There are no transactions!\n");
        }

        System.out.println("\n==ALL TRANSACTIONS==");
        for (Borrowing trx : transactions) {
            trx.printBorrowing();
            System.out.println();
        }
        System.out.println();
    }

    //
    public boolean isReturned() {
        return returned;
    }

    public void setReturned(Boolean bool) {
        if (Boolean.TRUE.equals(bool)) {
            returned = true;
            dateReturned = Calendar.getInstance();
        } else {
            returned = false;
            dateReturned = null;
        }
    }

    public boolean isBooksEmpty() {
        return books.isEmpty();
    }

    public boolean isBooksFull() {
        return books.size() >= maxBooks;
    }

    public boolean containsBook(Book bookCompare) {
        return books.contains(bookCompare);
    }

    public boolean addBook(Book book) {
        if (containsBook(book) || isBooksFull()) return false;

        books.add(book);
        return true;
    }

    public boolean removeBook(Book book) {
        return books.remove(book);
    }

    public void clearAllBooks() {
        books.clear();
    }
}

class Book {
    public static final Map<String, Book> map = new LinkedHashMap<>();

    private static int count = 0;
    private int id;

    private String title;
    private String details;
    private String publisher;

    //Constructor
    public Book(String title, String details, String publisher) {
        this.id = count++; // increment class global count

        this.title = title;
        this.details = details;
        this.publisher = publisher;
        map.put(title, this);
    }

    public void printBook() {
        System.out.printf("\"%s\" (ID %s)\nPublisher: %s\nDetails: %s\n",
                getTitle(), getId(), getPublisher(), getDetails());
    }

    // static method
    public static void printAllBooks() {
        System.out.println("\n--ALL BOOKS--");
        for (Book book : Book.map.values()) {
            book.printBook();
        }
        System.out.println();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getPublisher() {
        return publisher;
    }
}
