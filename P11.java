import java.sql.*;
import java.util.Scanner;

public class P11 {

    static final String URL = "jdbc:mysql://localhost:3306/books";
    static final String USER = "root";
    static final String PASS = "root";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASS);

            System.out.println("1. List All Authors");
            System.out.println("2. Books by Author");
            System.out.println("3. Authors by Book");
            System.out.print("Enter Choice: ");
            int ch = sc.nextInt();
            sc.nextLine();

            if (ch == 1) {

                String sql = "SELECT * FROM Authors ORDER BY lastName, firstName";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {
                    System.out.println(rs.getInt("authorID") + " "
                            + rs.getString("firstName") + " "
                            + rs.getString("lastName"));
                }

            } else if (ch == 2) {

                System.out.print("Enter Author ID: ");
                int id = sc.nextInt();

                String sql = "SELECT A.firstName,A.lastName,T.title,T.copyright,T.isbn "
                        + "FROM Authors A "
                        + "JOIN AuthorISBN AI ON A.authorID=AI.authorID "
                        + "JOIN Titles T ON AI.isbn=T.isbn "
                        + "WHERE A.authorID=?";

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, id);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    System.out.println("Author : " + rs.getString("firstName") + " " + rs.getString("lastName"));
                    System.out.println("Title  : " + rs.getString("title"));
                    System.out.println("Year   : " + rs.getString("copyright"));
                    System.out.println("ISBN   : " + rs.getString("isbn"));
                    System.out.println();
                }

            } else if (ch == 3) {

                System.out.print("Enter Book Title: ");
                String title = sc.nextLine();

                String sql = "SELECT A.firstName,A.lastName "
                        + "FROM Authors A "
                        + "JOIN AuthorISBN AI ON A.authorID=AI.authorID "
                        + "JOIN Titles T ON AI.isbn=T.isbn "
                        + "WHERE T.title=? "
                        + "ORDER BY A.lastName,A.firstName";

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, title);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    System.out.println(rs.getString("lastName") + ", "
                            + rs.getString("firstName"));
                }
            }

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}