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
                if (!student.isBorrowing()) {
                    System.out.println("You are not borrowing any books!");
                    break;
                }

                student.returnBooks();
                break;
            case 4: // Check Current Borrow
                if (!student.isBorrowing()) {
                    System.out.println("You are not borrowing any books!");
                    break;
                }

                student.getBorrow().printBorrowingInfo();   //Prints current transactions made by the user
                break;
            case 5: // logout
                return;
            default: // wrong input
                System.out.println("Please enter a valid choice!");
                break;
        }

    studentUserMenu(student);
    }

    public static void studentBorrowMenu(Student student){
        student.createBorrowing(); // initialize student.borrow
        String title;
        Book book;

        int choice = validateIntInput("(Add Book: 1, Remove Book: 2, View Cart: 3, Clear Cart: 4, Checkout: 5, Backout: 6)\nEnter choice: ");
        switch(choice){
            case 1: //Add
                // check if cart is full
                if (student.getBorrow().isBooksFull()) {
                    System.out.println("Your cart is already full!");
                    break;
                }

                // ask user the book they want added to cart
                student.getBorrow().printBorrowedBooks();
                title = getInput("Enter book title: ");
                book = Book.map.get(title);
                book.printBook();
                if (book == null) {
                    System.out.printf("Could not find \"%s\".\n", title);
                    break;
                }

                Boolean isAdded = student.getBorrow().addBook(book); // attempt to add book
                if (isAdded) {
                    System.out.printf("\"%s\" was added succesfully!\n", title);
                } else {
                    System.out.printf("\"%s\" was already added to the cart!\n", title);
                }
                break;
            case 2: // remove
                // check if cart is empty
                if (student.getBorrow().isBooksEmpty()) {
                    System.out.println("Your cart is already empty!");
                    break;
                }
                // ask user the book they want removed from cart
                student.getBorrow().printBorrowedBooks();
                title = getInput("Enter book title: ");
                book = Book.map.get(title);
                if (book == null) {
                    System.out.printf("Could not find \"%s\".\n", title);
                    break;
                }

                Boolean isRemoved = student.getBorrow().removeBook(book); // attempt to remove book
                if (isRemoved) {
                    System.out.printf("\"%s\" was removed succesfully!\n", title);
                } else {
                    System.out.printf("\"%s\" was not in your cart!\n", title);
                }
                break;
            case 3: // View cart
                student.getBorrow().printBorrowedBookTitles();
                break;
            case 4: // Clear cart
                student.getBorrow().clearAllBooks();
                System.out.println("Cart is cleared!");
                break;
            case 5: // Checkout
                // check if there are no books
                if (student.getBorrow().isBooksEmpty()) {
                    System.out.println("You cannot checkout 0 books!");
                    break;
                }

                System.out.println("Check out was succesful!");
                student.checkout(); // adds borrow to global transactions
                return;
            case 6: // backout
                student.removeBorrowing(); // makes student.borrow null
                return;
            default: // wrong input
                System.out.println("Please enter a valid choice!");
                break;
        }

        studentBorrowMenu(student);
    }

    // -- Functions to grab and validate input --

    // Validate that the name contains no numbers
    public static String validateNameInput(String msg) {
        String in = getInput(msg);

        if (in.matches(".*\\d.*")){
            System.out.println("Names should not contain a number!");
            return validateNameInput(msg);
        }

        return in;
    }

    // Validate that the inputted username doesn't already exist
    public static String validateUsernameInput(String msg) {
        String username = getInput(msg);

        if (User.doesUsernameExist(username)) {
            System.out.printf("%s already exists!");
            return validateUsernameInput(msg);
        }
        return username;
    }

    public static boolean validateBooleanInput() {
        String in = getInput("(True or False)\nEnter choice: ");

        if ("TRUE".equalsIgnoreCase(in)) return true;
        else if ("FALSE".equalsIgnoreCase(in)) return false;

        System.out.println("Incorrect input!");
        return validateBooleanInput();
    }

    public static int validateIntInput(String msg) {
        String in = getInput(msg);

        try {
            return Integer.parseInt(in);
        } catch(Exception e) {
            System.out.printf("%s is not an integer!\n", in);
            return validateIntInput(msg);
        }
    }

    public static String getInput(String msg) {
        System.out.print(msg);
        return input.nextLine();
    }
}