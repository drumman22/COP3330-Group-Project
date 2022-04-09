/*
UCF COP3330 Spring 2022
Group Project

Jason Grossman
Joseph Eddy
Brian Castro
*/

import java.util.*;

public class Main {
    static Scanner input = new Scanner(System.in);
    //private static final Map<String, User> userMap = new HashMap<>();

    public static void main(String[] args) {
        // test case

        // hasmap of username:user object
        // usernames will be unique
        new Student("Jason", 22, "student1", "password", Student.Status.JUNIOR);
        new Librarian("David", 33, "librarian", "apassword");

        System.out.println("Welcome to our Library");

        User user = loginMenu();

        // test code
        System.out.printf("Name: %s, Age: %s, Username: %s, Password: %s%n", user.name, user.age, user.username, user.password);
        if (user instanceof Student student) {
            // user is a student
            // update personal info

            // browse books
            //

            // borrow books
            //

            // return books

            // check borrows

            System.out.println("Student Status: " + student.status.toString());
        } 
        
        else if (user instanceof Librarian librarian) {
            // user is a librarian
            librarianUserMenu(librarian);
            System.out.print("\nTrue Librarian");
        } 
        
        else { System.out.print("False"); }

    }

    public static User loginMenu() {
        int choice = validateIntInput("(Login - 1, Create Student Account - 2)\nEnter choice: ");

        switch(choice) {
            case 1: return login();
            case 2: return createStudent();
            default:
                System.out.println("Please enter a valid choice!");
                return loginMenu();
        }
    }

    public static void librarianUserMenu(Librarian librarian) {

        // update student information
        // student name
        // student
        // check library transaction history
        // add book to library
        //Log off

        int choice = validateIntInput("(Update Student Info: 1, Transaction History: 2, Add New Book: 3, Logout: 4)\nEnter choice: ");

        switch(choice) {
            case 1: // update student info
            case 2: // transaction history
            case 3: // add a new book
            case 4: // logout
                break;
            default: // wrong input
                System.out.println("Please enter a vlid choice!");
                librarianUserMenu(librarian);
                break;
        }

        if (choice == 1){
            //update student info
        }
        else if(choice == 2){
            //look at transaction history
        }
        else if(choice == 3){
            //Create an object for book
            Book newBook = new Book();
            System.out.println("Enter the following");
            System.out.println("Title: ");
            String title = input.nextLine();
            System.out.println("Details: ");
            String details = input.nextLine();
            System.out.println("Publisher: ");
            String publisher = input.nextLine();

            //Call the method on the object
            //newBook.Book(String title, String details, String publisher);

        }
        else if(choice == 4){
            //log off
            System.out.println("Logging Off...");
            //Call main function
        }
        else{
            System.out.println("Please choose a valid option");
            //recursion call or call back to elseif statement for librarian
        }
    }

    // create new user account
    public static Student createStudent() {
        System.out.println("Creating a Student account, please enter the following information.");

        System.out.print("Enter a name: ");
        String name = input.nextLine();
        int age = validateIntInput("Enter your age: ");
        Student.Status status = Student.Status.fromInteger(validateIntInput("(Freshman: 1, Sophmore: 2, Junior: 3, Senior: 4)\nWhat is your Student Status: "));

        // check if users already exist?
        // check if username is not used
        System.out.print("Enter a username: ");
        String username = input.nextLine();
        System.out.print("Enter a password: ");
        String password = input.nextLine();

        Student newStudent = new Student(name, age, username, password, status);
        return newStudent;
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