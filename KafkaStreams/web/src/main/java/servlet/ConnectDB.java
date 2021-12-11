package servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectDB {

    private Connection conn;

    String INSERT_MANAGER = "INSERT INTO manager (email, nome) VALUES (?, ?)";

    public ConnectDB() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager
                    .getConnection("jdbc:postgresql://database:5432/project3",
                            "postgres", "My01pass");
            System.out.println("Conectado à DB com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }


    public void addManager(String email, String nome) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_MANAGER);
        ps.setString(1, email);
        ps.setString(2, nome);
        ps.executeUpdate();
        ps.close();
    }
}