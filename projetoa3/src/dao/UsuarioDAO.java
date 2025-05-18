package dao;

import java.sql.PreparedStatement;

import db.DBConnector;
import util.HashUtil;
import java.sql.*;

public class UsuarioDAO{
        public boolean cadastrarUsuario(String nome,String email,String senha, String telefone){
            String sql = "INSERT INTO usuario (nome,email,senha, telefone) VALUES (?,?,?,?)";
            try(Connection conn = DBConnector.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setString(1,nome);
                stmt.setString(2,email);
                stmt.setString(3,HashUtil.hash(senha));
                stmt.setString(4, telefone);
                stmt.executeUpdate();
                return true;
            }catch(SQLException e){
                System.out.println("Erro ao cadastrar: "+ e.getMessage());
                return false;
            }
        }

    public boolean loginUsuario(String email, String senha){
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
        try(Connection conn = DBConnector.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,email);
            stmt.setString(2,HashUtil.hash(senha));
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }catch(SQLException e){
            System.out.println("Erro ao logar: "+ e.getMessage());
            return false;
        }
    }

    public boolean verificarEmail(String email){
        String sql = "SELECT * FROM usuario WHERE email = ?";
        try(Connection conn = DBConnector.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}