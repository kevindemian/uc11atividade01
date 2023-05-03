/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ProdutosDAO {

    private conectaDAO conexao;
    private Connection conn;
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

    public void editar(ProdutosDTO p) {
        boolean b = false;
        String sql = "UPDATE produtos SET nome=?, status=?, valor=? WHERE id=?";
        try {

            PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getStatus());
            stmt.setInt(3, p.getValor());
            stmt.setInt(4, p.getId());

            stmt.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao editar: " + e.getMessage());
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

    public ProdutosDTO buscaProdutoPorId(int id_prod) {

        String sql = "SELECT * FROM produtos WHERE id=?";
        ProdutosDTO produto = null;

        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, id_prod);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                produto = new ProdutosDTO();
                produto.setId(id_prod);
                produto.setNome(rs.getString("nome"));
                produto.setStatus(rs.getString("status"));
                produto.setValor(rs.getInt("valor"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar produto por ID: " + e.getMessage());
        }

        return produto;

    }

    public void venderProduto(ProdutosDTO produto) {

        if ("vendido".equalsIgnoreCase(produto.getStatus())) {

            JOptionPane.showMessageDialog(null, "Esse produto já foi vendido!");

        } else if (produto != null) {

            produto.setStatus("Vendido");

            editar(produto);

            JOptionPane.showMessageDialog(null, "Venda realizada com sucesso!");

        } else {
            JOptionPane.showMessageDialog(null, "Produto não encontrado.");
        }
    }

}
