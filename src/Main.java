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
        // usernames must be unique
        new Student("Jason", 22, "student1", "password", Student.Status.JUNIOR);
        new Student("Austin", 25, "student2", "password", Student.Status.SENIOR);
        new Librarian("David", 33, "librarian", "apassword");

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
            // user is a student
            // update personal info

            // browse books
            //

            // borrow books
            //

            // return books

            // check borrows

            System.out.println("Student Status: " + student.getStatus().toString());
        } else if (user instanceof Librarian librarian) {
            // user is a librarian
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

    public static void librarianUserMenu(Librarian librarian) {
        int choice = validateIntInput("(Update Student Info: 1, View All Users: 2, Transaction History: 2, Add New Book: 3, Logout: 4)\nEnter choice: ");

        switch(choice) {
            case 1: // update student info
                Student student = getStudent(librarian);
                if (student == null) break;

                // else
                updateStudentInfoMenu(librarian, student);

                // print out the newly updated student account info
                System.out.printf("Updated Student Account Info\nUsername: %s, Name: %s, Age: %d\nStatus: %s, Enabled: %s, Created on: %s\n\n",
                        student.getUsername(), student.getName(), student.getAge(), student.getStatus().toString(), student.isEnabled(), student.getDateCreated().toString());
                break;
            case 2: // view all users
                for (User user : User.map.values()) {
                    System.out.printf("User Account: %s (%d), Created on: %s\nName: %s, Age: %d\n",
                            user.getUsername(), user.getId(), user.getDateCreated().toString(), user.getName(), user.getAge());

                    if (user instanceof Student studentUser) {
                        System.out.printf("Status: %s, Enabled: %s", studentUser.getStatus(), studentUser.isEnabled());
                    }
                    System.out.println();
                }
                break;
            case 3: // transaction history
                // TODO transaction history
                // loop through users like case 2 but ignore librarians
                // lirbarians shouldnt have the borrow transaction class
            case 4: // add a new book
                // TODO
            case 5: // logout
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
        System.out.printf("Student Account Info\nUsername: %s, Name: %s, Age: %s\nStatus: %s, Enabled: %s, Created on: %s\n",
                student.getUsername(), student.getName(), student.getAge(), student.getStatus().toString(), student.isEnabled(), student.getDateCreated().toString());

        return student;
    }
    // might not need to pass librarian
    public static void updateStudentInfoMenu(Librarian librarian, Student student) {
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
                updateStudentInfoMenu(librarian, student);
                break;
        }

        updateStudentInfoMenu(librarian, student);
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
        System.out.print("Enter a username: ");
        String username = input.nextLine();
        System.out.print("Enter a password: ");
        String password = input.nextLine();

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

    public static boolean validateBooleanInput() {
        String in = getInput("(True or False)\nEnter choice: ");

        if ("TRUE".equalsIgnoreCase(in)) return true;
        else if ("FALSE".equalsIgnoreCase(in)) return false;

        System.out.println("Incorrect input!");
        return validateBooleanInput();
    }

    public static String validateNameInput(String msg) {
        String in = getInput(msg);

        if (in.matches(".*\\d.*")){
            System.out.println("Names should not contain a number!");
            return validateNameInput(msg);
        }

        return in;
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