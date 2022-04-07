/*
UCF COP3330 Spring 2022
Group Project

Jason Grossman
Joseph Eddy
Brian Castro
*/

import java.util.Date;
import java.util.Scanner;

public class Library {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        // test case
        System.out.println("Welcome to our Library");
        User[] users = {new Student("Jason", 22, "username", "password"), new Librarian("David", 33, "ausername", "apassword")};
        User user = loginMenu(users);

        // test code
        System.out.printf("Name: %s, Age: %s, Username: %s, Password: %s", user.name, user.age, user.username, user.password);
        if (user instanceof Student) {
            System.out.print("\nTrue Student");
        } else if (user instanceof Librarian) {
            System.out.print("\nTrue Librarian");
        } else { System.out.print("False"); }

    }

    public static User loginMenu(User[] users) {
        int choice = validateIntInput("Login: 1\nCreate Account: 2\nEnter choice: ");

        if (choice == 1) {
            return login(users);
        }
        else if (choice == 2) {
            return create();
        }

        // this shouldnt happen
        else {
            System.out.println("Something went wrong");
            return loginMenu(users);
        }
    }

    public static User create() {
        System.out.println("Creating a user, please enter the following information.");

        System.out.print("Enter a name: ");
        String name = input.nextLine();
        int age = validateIntInput("Enter your age: ");
        Student.Status status = getStudentStatusEnum(validateIntInput("(Freshman: 1, Sophmore: 2, Junior: 3, Senior: 4)\nWhat is your Student Status: "));

        // check if users already exist?
        // check if username is not used
        System.out.print("Enter a username: ");
        String username = input.nextLine();
        System.out.print("Enter a password: ");
        String password = input.nextLine();

        return new Student(status, new Date(), name, age, username, password);
    }

    public static Student.Status getStudentStatusEnum(int n) {
        return switch (n) {
            case 1 -> Student.Status.FRESHMAN;
            case 2 -> Student.Status.SOPHMORE;
            case 3 -> Student.Status.JUNIOR;
            case 4 -> Student.Status.SENIOR;
            default -> Student.Status.FRESHMAN;
        };
    }

    public static User login(User[] users) {
        System.out.print("Enter username: ");
        String username = input.nextLine();
        System.out.print("Enter password: ");
        String password = input.nextLine();

        // find user that validates login
        for (User user: users) {
            if (user.loginBool(username, password)) return user;
        }

        // if none validate login then run loginmenu again
        System.out.println("Incorrect username or password!");
        return loginMenu(users);
    }

    public static int validateIntInput(String msg) {
        System.out.print(msg);
        String in = input.nextLine();

        try {
            return Integer.parseInt(in);
        } catch(Exception e) {
            System.out.printf("%s is not an integer!\n", in);
            return validateIntInput(msg);
        }
    }
}

// Jason
class User {
    protected int id;
    public String name;
    public int age;
    protected String username;
    protected String password;

    public User(String name, int age, String username, String password) {
        this.name = name;
        this.age = age;
        this.username = username;
        this.password = password;
    }

//    public static User create() {
//
//    }

    public boolean loginBool(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void update() {

    }
}

// Jason
class Librarian extends User {
    // public String department;

    public Librarian(String name, int age, String username, String password) {
        super(name, age, username, password);
    }

    protected void enableUser() {

    }

    protected void disableUser() {

    }
}


//Brian
class Student extends User {
    public enum Status {
        FRESHMAN,
        SOPHMORE,
        JUNIOR,
        SENIOR
    }

    public Status status;
    public Date dateCreated; // date account was created

    public Student(String name, int age, String username, String password) {
        this(Status.FRESHMAN, new Date(), name, age, username, password);
    }

    public Student(Status status, Date dateCreated, String name, int age, String username, String password) {
        super(name, age, username, password);
        this.status = status;
        this.dateCreated = dateCreated;
    }

    public String getStatus() {
        return switch (status) {
            case FRESHMAN -> "Freshman";
            case SOPHMORE -> "Sophomore";
            case JUNIOR -> "Junior";
            case SENIOR -> "Senior";
            default -> "None";
        };
    }

    public void browseBooks() {

    }
}

//Brian
class Transaction {
    protected int id;
    public String details;
    // date: String
    public Borrowing borrowing;

    public void update() {

    }
}

//Joe
class Borrowing {
    protected int id;
    // bookname: String
    public Book[] books;
    // borrower: String
    public Student borrower;
    // dateborrowed: String
    public Date dateBorrowed;
    // dateDue String
    public Date dateDue;

    public void create(){
    //Create a 
        
    }
    public void update(){

    }
}

//Joe
class Book {
    protected int id;
    public String title;
    public String details;
    public String publisher;

    public void add(){

    }

    public void update(){

    }
}
