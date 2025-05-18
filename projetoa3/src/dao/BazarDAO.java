package dao;
import db.DBConnector;
import java.sql.*;
import java.sql.PreparedStatement;
import util.HashUtil;

public class BazarDAO {
    public boolean cadastrarBazar(String nome, String endereco, String cnpj, String email_contato) {
        String sql = "INSERT INTO bazar (nome, cnpj, endereco, email_contato) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnector.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, cnpj);
            stmt.setString(3, endereco);
            stmt.setString(4, email_contato);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar bazar: " + e.getMessage());
            return false;
        }
    }

    public boolean cadastrarBazar(String nome, String cnpj, String endereco, String email_contato, String senha) {
        String sql = "INSERT INTO bazar (nome, cnpj, endereco, email_contato, senha) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnector.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, cnpj);
            stmt.setString(3, endereco);
            stmt.setString(4, email_contato);
            stmt.setString(5, HashUtil.hash(senha));
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar bazar: " + e.getMessage());
            return false;
        }
    }

    public boolean login(String email, String senha) {
        String sql = "SELECT * FROM bazar WHERE email_contato = ? AND senha = ?";
        try (Connection conn = DBConnector.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, HashUtil.hash(senha));
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Erro ao logar bazar: " + e.getMessage());
            return false;
        }
    }

    public boolean verificarEmail(String email) {
        String sql = "SELECT * FROM bazar WHERE email_contato = ?";
        try (Connection conn = DBConnector.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Erro ao verificar email: " + e.getMessage());
            return false;
        }
    }

    public boolean verificarCNPJ(String cnpj) {
        String sql = "SELECT * FROM bazar WHERE cnpj = ?";
        try (Connection conn = DBConnector.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cnpj);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Erro ao verificar CNPJ: " + e.getMessage());
            return false;
        }
    }

    public boolean verificarNomeBazar(String nome) {
        String sql = "SELECT * FROM bazar WHERE nome = ?";
        try (Connection conn = DBConnector.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Erro ao verificar nome do bazar: " + e.getMessage());
            return false;
        }
    }   
}
