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

public class Main {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        // hasmap of username:user object
        // usernames should be unique
        new Student("Jason", 22, "student1", "password", Student.Status.JUNIOR);
        new Student("Austin", 25, "student2", "password", Student.Status.SENIOR);
        new Librarian("David", 33, "librarian", "apassword");

        new Book("My Book", "Me", "N/A");
        new Book("My second book", "Me", "Some details");

        System.out.println("Welcome to our Library");

        boolean run = true;
        while (run) {
            run = runLibrary();
        }

        System.out.println("Exiting program..");
    }

    public static boolean runLibrary() {
        User user = loginMenu();

        // end program
        if (user == null) return false;

        // test code
        System.out.printf("Name: %s, Age: %s, Username: %s, Password: %s\n", user.getName(), user.getAge(), user.username, user.password);
        if (user instanceof Student student) {
            // user is a student, run student menu
            studentUserMenu(student);
        } else if (user instanceof Librarian librarian) {
            // user is a librarian, run librarian menu
            librarianUserMenu(librarian);
        } else {
            System.out.print("False");
        }

        return true;
    }

    public static User loginMenu() {
        int choice = validateIntInput("(Login: 1, Create Student Account: 2, Exit Program: 3)\nEnter choice: ");

        switch(choice) {
            case 1: return login();
            case 2: return createStudent();
            case 3: return null;
            default:
                System.out.println("Please enter a valid choice!");
                return loginMenu();
        }
    }

    // create new user account
    public static Student createStudent() {
        System.out.println("Creating a Student account, please enter the following information.");

        System.out.print("Enter a name: ");
        String name = input.nextLine();
        int age = validateIntInput("Enter your age: ");
        Student.Status status = Student.Status.fromInteger(validateIntInput("(Freshman: 1, Sophmore: 2, Junior: 3, Senior: 4, Graduate: )\nWhat is your Student Status: "));

        // check if users already exist?
        // check if username is not used
        String username = validateUsernameInput("Enter a username: ");
        String password = getInput("Enter a password");

        return new Student(name, age, username, password, status);
    }

    // attempt user login
    public static User login() {
        System.out.print("Enter username: ");
        String username = input.nextLine();
        System.out.print("Enter password: ");
        String password = input.nextLine();

        // find user that validates login
        User user = User.map.get(username);
        if (user != null && user.loginBool(username, password))
            return user;

        // if none validate login then run loginmenu again
        System.out.println("Incorrect username or password!");
        return loginMenu();
    }

    // -- Librarian Menu and Functions --

    public static void librarianUserMenu(Librarian librarian) {
        int choice = validateIntInput("(Update Student Info: 1, View All Users: 2, Transaction History: 3, Add New Book: 4, Remove Book: 5, Logout: 6)\nEnter choice: ");

        switch(choice) {
            case 1: // update student info
                Student student = getStudent(librarian);
                if (student == null) break; // break if student doesn't exist

                // else go to update menu
                updateStudentInfoMenu(student);
                student.printUser(); // print student info
                break;
            case 2: // view all users
                User.printAllUsers();
                break;
            case 3: // transaction history
                // TODO transaction history
                // loop through users like case 2 but ignore librarians
                // lirbarians shouldnt have the borrow transaction class

                Borrowing.printAllTransactions();
                break;
            case 4: // add a new book
                Book newBook = new Book(getInput("Enter book title: "), getInput("Enter book publisher: "), getInput("Enter book details: "));
                System.out.printf("\"%s\" has been successfully added!\n", newBook.getTitle());
                break;
            case 5: // remove book from map
                String title = getInput("Enter book title (case sensitive): ");
                Book book = Book.map.get(title);
                if (book == null) {
                    System.out.printf("Could not find \"%s\".\n", title);
                }

                book.map.remove(title);
                System.out.printf("\"%s\" was successfully removed!\n", title);
            case 6: // logout
                return;
            default: // wrong input
                System.out.println("Please enter a valid choice!");
                break;
        }

        librarianUserMenu(librarian);
    }

    public static Student getStudent(Librarian librarian) {
        System.out.print("Enter Student's username: ");
        String username = input.nextLine();

        User user = User.map.get(username);
        if (user == null || user instanceof Librarian) {
            System.out.println("Could not find Student!");
            librarianUserMenu(librarian);
            return null;
        }

        Student student = (Student) user;
        student.printUser();
        return student;
    }

    public static void updateStudentInfoMenu(Student student) {
        int choice = validateIntInput("(Name: 1, Age: 2, Status: 3, Enabled: 4, Backout: 5)\nEnter choice: ");
        switch(choice) {
            case 1: // update name
                student.setName(validateNameInput("Enter name: "));
                break;
            case 2: // update age
                student.setAge(validateIntInput("Enter age: "));
                break;
            case 3: // update status
                student.setStatus(Student.Status.fromInteger(validateIntInput("(Freshman: 1, Sophmore: 2, Junior: 3, Senior: 4, Graduate: 5)\nWhat is your Student Status: ")));
                break;
            case 4: // change enable
                student.setEnabled(validateBooleanInput());
                break;
            case 5: // Backout
                return;
            default: // wrong input
                System.out.println("Please enter a valid choice!");
                updateStudentInfoMenu(student);
                break;
        }

        updateStudentInfoMenu(student);
    }

    // -- Student Menus and Functions --

    public static void studentUserMenu(Student student) {
        int choice = validateIntInput("(Browse books: 1, Borrow books: 2, Return books: 3, Check borrowed books: 4, logout: 5,)\nEnter choice: ");
        switch(choice) {
            case 1: // Browse books
                Book.printAllBooks();
                break;
            case 2: // Borrow books
                if (student.isBorrowing()) {
                    System.out.println("You are already borrowing books!");
                    break;
                }
                // else
                studentBorrowMenu(student);
                break;
            case 3: // Return books
                // TODO

            case 4: // Check

            case 5: // logout
                return;
            default: // wrong input
                System.out.println("Please enter a valid choice!");
                break;
        }

    studentUserMenu(student);
    }
}