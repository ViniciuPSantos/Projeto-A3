// src/dao/ProdutoDAO.java
package dao;

import db.DBConnector;
import model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    /**
     * Busca todos os produtos do banco de dados.
     * @return Uma lista de objetos Produto. Retorna uma lista vazia se não houver produtos ou em caso de erro.
     */
    public List<Produto> getAllProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT id, nome, tamanho, valor_unitario, estoque FROM produtos"; // Sua query SQL
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnector.getConnection(); // Obtém uma conexão
            stmt = conn.prepareStatement(sql); // Prepara a query
            rs = stmt.executeQuery(); // Executa a query e obtém os resultados

            while (rs.next()) { // Itera sobre cada linha do resultado
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String tamanho = rs.getString("tamanho");
                double valorUnitario = rs.getDouble("valor_unitario");
                int estoque = rs.getInt("estoque"); // Se você tiver o campo estoque

                // Cria um objeto Produto com os dados do banco
                produtos.add(new Produto(id, nome, tamanho, valorUnitario, estoque));
            }
        } catch (SQLException e) {
            // Imprime o erro no console (para debug)
            System.err.println("Erro ao buscar produtos do banco de dados: " + e.getMessage());
            // Você pode adicionar um showMsg aqui para o usuário, mas é melhor no CarrinhoApp
        } finally {
            // Garante que a conexão e os recursos sejam fechados
            DBConnector.closeConnection(conn, stmt, rs);
        }
        return produtos;
    }

    // Opcional: método para buscar um produto por ID, útil se você quiser mais detalhes
    public Produto getProdutoById(int id) {
        Produto produto = null;
        String sql = "SELECT id, nome, tamanho, valor_unitario, estoque FROM produtos WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnector.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id); // Define o valor para o placeholder '?'
            rs = stmt.executeQuery();

            if (rs.next()) { // Se encontrar o produto
                String nome = rs.getString("nome");
                String tamanho = rs.getString("tamanho");
                double valorUnitario = rs.getDouble("valor_unitario");
                int estoque = rs.getInt("estoque");
                produto = new Produto(id, nome, tamanho, valorUnitario, estoque);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por ID: " + e.getMessage());
        } finally {
            DBConnector.closeConnection(conn, stmt, rs);
        }
        return produto;
    }
}
