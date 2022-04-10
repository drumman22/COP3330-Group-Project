/*
UCF COP3330 Spring 2022
Group Project

Jason Grossman
Joseph Eddy
Brian Castro
*/

import java.util.*;

class User {
    public static final Map<String, User> map = new LinkedHashMap<>();

    private static int count = 0;
    protected int id;

    private String name;
    private int age;
    protected String username;
    protected String password;
    private final Date dateCreated; // date account was created

    public User(String name, int age, String username, String password) {
        // TODO ensure that the username is unique compared to the user map

        this.id = count++; // increment global count

        this.name = name;
        this.age = age;
        this.username = username;
        this.password = password;
        this.dateCreated = new Date();

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

    public Date getDateCreated() { return dateCreated; }

    public boolean loginBool(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
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
    // TODO add a borrow array attribute, limit it to 0..3

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

    public void browseBooks() {

    }
}

// may not be using this
class Transaction {
    protected int id;
    public Date returned;

    public Borrowing borrowing;

    public void update() {

    }
}

class Borrowing {
    private static int count = 0;
    public int id;

    public Book[] books;
    public Student borrower;
    public Calendar dateBorrowed;
    public Calendar dateDue;
    public Calendar dateReturned;
    public Boolean returned;

    public Borrowing(Student borrower){
        this.id = count++; // increment class global count

        this.dateBorrowed = Calendar.getInstance();
        this.dateDue = Calendar.getInstance();
        this.dateDue.add(Calendar.DAY_OF_YEAR, 30); // set due date to 30 days from today

        this.borrower = borrower;
        this.returned = false;
    }

    public boolean isReturned() {
        return returned;
    }

    public void addBook() {

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

    public void update(){
        //Setter

    }
}

class Book {
    private static int count = 0;
    public int id;

    public String title;
    public String details;
    public String publisher;


    //Constructor
    public void add(String title, String details, String publisher){
        this.id = count++; // increment class global count

        this.title = title;
        this.details = details;
        this.publisher = publisher;

    }

    public void update(){
        //return book
    }
}
