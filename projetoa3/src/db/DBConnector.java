package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/banco_projetoa3";
    private static final String USER = "root"; 
    private static final String PASSWORD = "senha"; // Substitua pela sua senha

    public static Connection conectar() throws SQLException{
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/banco_projetoa3", "root", "vini280706_@Vp");
    }
}