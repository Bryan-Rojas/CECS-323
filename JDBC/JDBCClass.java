
import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mimi Opkins with some tweaking from Dave Brown
 */
public class JDBCClass {

    static Scanner in = new Scanner(System.in);
    static boolean valid;
    //  Database credentials
    static String USER = "Mateo";
    static String PASS = "Mateo";
    static String DBNAME = "JDBC";
// JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
//            + "testdb;user=";
    static Connection conn = null;
    static Statement stmt = null;
    static ResultSet rs = null;

    /**
     * Takes the input string and outputs "N/A" if the string is empty or null.
     *
     * @param input The string to be mapped.
     * @return Either the input string or "N/A" as appropriate.
     */
    public static String dispNull(String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0) {
            return "N/A";
        } else {
            return input;
        }
    }

    public static void main(String[] args) {
        //Prompt the user for the database name, and the credentials.
        //If your database has no credentials, you can update this code to
        //remove that from the connection string.

        /*System.out.print("Name of the database (not the user account): ");
        DBNAME = in.nextLine();
        System.out.print("Database user name: ");
        USER = in.nextLine();
        System.out.print("Database password: ");
        PASS = in.nextLine();*/
        //Constructing the database URL connection string
        DB_URL = DB_URL + DBNAME + ";user=" + USER + ";password=" + PASS;

        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();

            //initialize the selection and flags
            int selection = 0;
            boolean inProgram = true;
            valid = false;
            System.out.println("Welcome to our database!");

            do {
                while (!valid) {
                    System.out.println("Select an option: \n"
                            + "1: List all writing groups\n"
                            + "2: List all the data for a group specified by the user\n"
                            + "3: List all publisher\n"
                            + "4: List all the data for a publisher\n"
                            + "5: List all book titles\n"
                            + "6: List all data for a specific book\n"
                            + "7: Insert a new book\n"
                            + "8: Replace a publisher\n"
                            + "9: Remove a book\n"
                            + "10: Exit");

                    if (in.hasNextInt()) {//check that a number was used
                        selection = in.nextInt();
                        in.nextLine();
                        if (selection > 0 && selection < 11)//check if a vailid number is used
                        {
                            valid = true;
                        } else {
                            System.out.println("Input a valid selection");
                            valid = false;
                        }
                    } else {
                        System.out.println("You must input a valid selection");
                        in.nextLine();
                        valid = false;
                    }
                }
                //Show all writing groups
                if (selection == 1) {
                    listAllGroups();
                } else if (selection == 2) {
                    listSpecifiedGroup();
                } else if (selection == 3) {
                    listAllPublishers();
                } else if (selection == 4) {
                    listSpecifiedPublisher();
                } else if (selection == 5) {
                    listAllBooks();
                } else if (selection == 6) {
                    listSpecifiedBook();
                } else if (selection == 7) {
                    addNewBook();
                } else if (selection == 8) {
                    addPublisher();
                } else if (selection == 9) {
                    removeBook();
                } else {
                    System.out.println("Thank you! Goodbye");
                    inProgram = false;
                }
            } while (inProgram);

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException s) {
            System.out.println("\n" + s.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("\n" + e.getMessage() + "\n");
        }

    }//end main

    private static void listAllGroups() {

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM writingGroup");

            System.out.printf("%-30s%-30s%-30s%-30s\n", "Group Name", "Head Writer", "Year Formed", "Subject");

            while (rs.next()) {
                String gName = rs.getString("groupName");
                String hWriter = rs.getString("headWriter");
                String yFormed = rs.getString("yearFormed");
                String subject = rs.getString("subject");

                System.out.printf("%-30s%-30s%-30s%-30s\n", dispNull(gName), dispNull(hWriter), dispNull(yFormed), dispNull(subject));
                valid = false;
            }
        } catch (SQLException ex) {
            System.out.println("\n" + ex.getMessage() + "\n");
        }
    }

    private static void listSpecifiedGroup() {
        try {
            String gInput;
            System.out.println("Enter the name of the Group you would like more info on: ");
            gInput = in.nextLine();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT groupName, bookTitle, publisherName, yearPublished, numberPages, "
                    + "publisherAddress, publisherPhone, publisherEmail, headWriter, yearFormed, subject "
                    + "FROM writingGroup INNER JOIN BOOK using (groupName) INNER JOIN PUBLISHER using (publisherName) WHERE groupName = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gInput);
            rs = pstmt.executeQuery();
            if (rs.next() == false) {
                System.out.println("Invalid group!");
                listSpecifiedGroup();
            } else {
                System.out.printf("%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s\n", "Group Name", "Head Writer", "Year Formed", "Subject",
                        "Publisher", "Publisher Address", "Publisher Phone", "Publisher Email",
                        "Book Title", "Year Published", "Pages");

                String pName = rs.getString("publisherName");
                String pAddress = rs.getString("publisherAddress");
                String pPhone = rs.getString("publisherPhone");
                String pEmail = rs.getString("publisherEmail");
                String bTitle = rs.getString("bookTitle");
                String yPub = rs.getString("yearPublished");
                String pages = rs.getString("numberpages");
                String gName = rs.getString("groupName");
                String hWriter = rs.getString("headWriter");
                String yFormed = rs.getString("yearFormed");
                String subject = rs.getString("subject");

                System.out.printf("%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s\n", dispNull(gName),
                        dispNull(hWriter), dispNull(yFormed), dispNull(subject), dispNull(pName), dispNull(pAddress),
                        dispNull(pPhone), dispNull(pEmail), dispNull(bTitle), dispNull(yPub), dispNull(pages));
            }
            valid = false;
        } catch (SQLException ex) {
            System.out.println("\n" + ex.getMessage() + "\n");
        }
    }

    private static void listAllPublishers() {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM publisher");

            System.out.printf("%-30s%-30s%-30s%-30s\n", "Publisher Name", "Publisher Address", "Publisher Phone", "Publisher Email");
            while (rs.next()) {
                String pName = rs.getString("publisherName");
                String pAddress = rs.getString("publisherAddress");
                String pPhone = rs.getString("publisherPhone");
                String pEmail = rs.getString("publisherEmail");

                System.out.printf("%-30s%-30s%-30s%-30s\n", dispNull(pName), dispNull(pAddress), dispNull(pPhone), dispNull(pEmail));
                valid = false;
            }
        } catch (SQLException ex) {
           System.out.println("\n" + ex.getMessage() + "\n");
        }
    }

    private static void listSpecifiedPublisher() {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String pInput;
            String sql = "SELECT publisherName, publisherAddress, publisherPhone,"
                    + "publisherEmail, bookTitle, yearPublished, numberPages,"
                    + "groupName, headWriter, yearFormed, subject FROM Publisher "
                    + "INNER JOIN BOOK using (PUBLISHERNAME)"
                    + "INNER JOIN WRITINGGROUP using (GROUPNAME) WHERE publisherName = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            System.out.println("Enter the name of the publisher you would like more info on: ");
            pInput = in.nextLine();
            pstmt.setString(1, pInput);
            rs = pstmt.executeQuery();
            if (rs.next() == false) {
                System.out.println("Invalid publisher!");
                listSpecifiedPublisher();
            } else {
                System.out.printf("%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s\n", "Publisher", "Publisher Address",
                        "Publisher Phone", "Publisher Email", "Book Title", "Year Published",
                        "Pages", "Group Name", "HeadWriter", "Year Formed", "Subject");
                String pName = rs.getString("publisherName");
                String pAddress = rs.getString("publisherAddress");
                String pPhone = rs.getString("publisherPhone");
                String pEmail = rs.getString("publisherEmail");
                String bTitle = rs.getString("bookTitle");
                String yPub = rs.getString("yearPublished");
                String pages = rs.getString("numberpages");
                String gName = rs.getString("groupName");
                String hWriter = rs.getString("headWriter");
                String yFormed = rs.getString("yearFormed");
                String subject = rs.getString("subject");

                System.out.printf("%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s%-30s\n", dispNull(pName), dispNull(pAddress), dispNull(pPhone), dispNull(pEmail), dispNull(bTitle), dispNull(yPub), dispNull(pages), dispNull(gName),
                        dispNull(hWriter), dispNull(yFormed), dispNull(subject));
            }
            valid = false;
        } catch (SQLException ex) {
            System.out.println("\n" + ex.getMessage() + "\n");
        }
    }

    private static void listAllBooks() {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM book");

            System.out.printf("%-30s%-30s%-30s%-30s%-30s\n", "Group Name", "Book Title", "Publisher Name", "Year Published", "Pages");
            while (rs.next()) {
                String gName = rs.getString("groupName");
                String bTitle = rs.getString("bookTitle");
                String pName = rs.getString("publisherName");
                String year = rs.getString("yearPublished");
                String pages = rs.getString("numberPages");

                System.out.printf("%-30s%-30s%-30s%-30s%-30s\n", dispNull(gName), dispNull(bTitle), dispNull(pName), dispNull(year), dispNull(pages));

            }
            valid = false;
        } catch (SQLException ex) {
            System.out.println("\n" + ex.getMessage() + "\n");
        }
    }

    private static void listSpecifiedBook() {
        try {
            String bInput;
            System.out.println("Enter the name of the Book you would like more info on: ");
            bInput = in.nextLine();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT * FROM book WHERE bookTitle = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bInput);
            rs = pstmt.executeQuery();
            if (rs.next() == false) {
                System.out.println("Invalid book!");
                listSpecifiedBook();
            } else {
                System.out.printf("%-30s%-30s%-30s%-30s%-30s\n", "Group Name", "Book Title", "Publisher Name", "Year Published", "Pages");

                String gName = rs.getString("groupName");
                String bTitle = rs.getString("bookTitle");
                String pName = rs.getString("publisherName");
                String year = rs.getString("yearPublished");
                String pages = rs.getString("numberPages");

                System.out.printf("%-30s%-30s%-30s%-30s%-30s\n", dispNull(gName), dispNull(bTitle), dispNull(pName), dispNull(year), dispNull(pages));
            }
            valid = false;
        } catch (SQLException ex) {
            System.out.println("\n" + ex.getMessage() + "\n");
        }
    }

    private static void addNewBook() {
        System.out.println("Enter groupName: ");
        String gName = in.nextLine();

        System.out.println("Enter bookTitle: ");
        String bTitle = in.nextLine();

        System.out.println("Enter publisherName: ");
        String pName = in.nextLine();

        System.out.println("Enter the yearPublished: ");
        String yPub = in.nextLine();

        System.out.println("Enter the numberPages: ");
        String pNum = in.nextLine();

        String sql = "INSERT INTO Book (groupName, bookTitle, publisherName, yearPublished, numberPages) values (?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gName);
            pstmt.setString(2, bTitle);
            pstmt.setString(3, pName);
            pstmt.setString(4, yPub);
            pstmt.setString(5, pNum);
            pstmt.execute();
            
            listAllBooks();
            
            valid = false;
        } catch (SQLException ex) {
            System.out.println("\n" + ex.getMessage() + "\n");
        }
    }

    private static void addPublisher() {
        String pName;
        String pAddress;
        String pPhone;
        String pEmail;
        System.out.println("Enter new publisher's name: ");
        pName = in.nextLine();
        System.out.println("Enter new publisher's address: ");
        pAddress = in.nextLine();
        System.out.println("Enter new publisher's Phone: ");
        pPhone = in.nextLine();
        System.out.println("Enter new publisher's email address: ");
        pEmail = in.nextLine();

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "INSERT INTO Publisher VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pName);
            pstmt.setString(2, pAddress);
            pstmt.setString(3, pPhone);
            pstmt.setString(4, pEmail);
            pstmt.executeUpdate();
            pstmt.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("\n" + ex.getMessage() + "\n");
        }
        replacePublisher(pName);
        valid = false;
    }

    private static void removeBook() {
        String bookName;
        String groupName;
        System.out.println("Enter the name of the group the book belongs to:");
        groupName = in.nextLine();
        System.out.println("Enter the name of the book to remove:");
        bookName = in.nextLine();
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "DELETE FROM book WHERE booktitle = ? AND groupName = ?";
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookName);
            pstmt.setString(2, groupName);
            int deleted = pstmt.executeUpdate();
            if (deleted <= 0) {
                System.out.println("ERROR. No such book exists in the database.");
            } else {
                System.out.println("The book: " + bookName + " has been deleted!");
                listAllBooks();
            }

            pstmt.close();
            stmt.close();
            conn.close();

            valid = false;

        } catch (SQLException ex) {
            System.out.println("\n" + ex.getMessage() + "\n");
        }

    }

    private static void replacePublisher(String pName) {
        String rPub;
        System.out.println("Enter the name of the publisher that will be bought out by the new publisher");
        rPub = in.nextLine();
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "UPDATE Book SET publisherName = ? WHERE publisherName = ?";
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pName);
            pstmt.setString(2, rPub);
            pstmt.executeUpdate();
            System.out.println(rPub + " has been bought out by " + pName);
            rs = stmt.executeQuery("SELECT * FROM book WHERE publisherName = '" + pName + "'");

            System.out.printf("%-30s%-30s%-30s%-30s%-30s\n", "Group Name", "Book Title", "Publisher Name", "Year Published", "Pages");
            while (rs.next()) {
                String gName = rs.getString("groupName");
                String bTitle = rs.getString("bookTitle");
                String pubName = rs.getString("publisherName");
                String year = rs.getString("yearPublished");
                String pages = rs.getString("numberPages");

                System.out.printf("%-30s%-30s%-30s%-30s%-30s\n", dispNull(gName), dispNull(bTitle), dispNull(pName), dispNull(year), dispNull(pages));

            }
            pstmt.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("\n" + ex.getMessage() + "\n");
        }
    }

}
