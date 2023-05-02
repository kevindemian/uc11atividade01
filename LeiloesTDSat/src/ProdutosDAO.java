/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */
import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutosDAO {

    private conectaDAO conexao;
    private Connection conn;
    private PreparedStatement prep;
    private ResultSet resultset;
    private ArrayList<ProdutosDTO> listagem = new ArrayList<>();

    public ProdutosDAO() {
        this.conexao = new conectaDAO();
        this.conn = this.conexao.connectDB();
    }

    public int cadastrarProduto(ProdutosDTO produto) {
        int status;
        String sql = "INSERT INTO produtos (nome, valor, status) VALUES(?, ?, ?)";
        try {

            PreparedStatement st = this.conn.prepareStatement(sql);
            st.setString(1, produto.getNome());
            st.setInt(2, produto.getValor());
            st.setString(3, produto.getStatus());
            status = st.executeUpdate();
            return status;
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar: " + ex.getMessage());
            return ex.getErrorCode();
        }

    }

    public ArrayList<ProdutosDTO> listarProdutos() {

        String sql = "SELECT * FROM produtos";
        ArrayList<ProdutosDTO> listaProdutos = new ArrayList<>();
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setValor(rs.getInt("valor"));
                produto.setStatus(rs.getString("status"));
                listaProdutos.add(produto);
            }

        } catch (SQLException e) {
            System.out.println("erro: " + e.getMessage());
            return null;
        }

        return listaProdutos;
    }

    public void desconectar() {
        try {
            conn.close();
        } catch (SQLException ex) {
        }
    }

}
