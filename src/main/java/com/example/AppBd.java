package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AppBd {
    private static final String PASSWORD = "";
    private static final String USERNAME = "gitpod";
    private static final String JDBC_URL = "jdbc:postgresql://localhost/postgres";
    
    public static void main(String[] args) {
        new AppBd();
    }

    public AppBd(){
        try (var conn = getConnection()){
            listarEstados(conn);
            localizarEstado(conn, "PR");

            var marca = new Marca();
            marca.setId(3L);

            var produto = new Produto();
            produto.setId(203L);
            produto.setMarca(marca);
            produto.setValor(190);
            produto.setNome("Produto Novo");
            
            //inserirProduto(conn, produto);
            alterarProduto(conn, produto);
            excluirProduto(conn, 202L);
            listarDadosTabela(conn, "produto");
        } catch (SQLException e){
            System.err.println("nao foi possivel carregar"+ e.getMessage());
        }
    }

    private void excluirProduto(Connection conn ,long id) {
        var sql = ("delete from produto where id = ?");
        try {
            var statement = conn.prepareStatement(sql);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("nao foi possivel excluir"+ e.getMessage());
        }
        
    }

    /*private void inserirProduto(Connection conn, Produto produto) {
        var sql = "insert into produto(nome, marca_id, valor) values (?, ?, ?)";
        try (var statement = conn.prepareStatement(sql)){
            statement.setString(1, produto.getNome());
            statement.setLong(2, produto.getMarca().getId());
            statement.setDouble(3, produto.getValor());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("nao foi possivel carregar"+ e.getMessage());
        }
    }*/

    private void alterarProduto(Connection conn, Produto produto) {
        var sql = "update produto set nome = ?, marca_id = ?, valor = ? where id = ?";
        try (var statement = conn.prepareStatement(sql)){
            statement.setString(1, produto.getNome());
            statement.setLong(2, produto.getMarca().getId());
            statement.setDouble(3, produto.getValor());
            statement.setLong(4, produto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro na alteração do produto"+ e.getMessage());
        }
    }

    private void listarDadosTabela(Connection conn, String tabela) {
        var sql = "select * from "+ tabela ;
        System.out.println(sql);
        try {
            var statement = conn.createStatement();
            var result = statement.executeQuery(sql);
            var metadata = result.getMetaData();
            int cols = metadata.getColumnCount();
            for (int i = 1; i<=cols; i++){
                System.out.printf("%-25s |", metadata.getColumnName(i));
            }
            System.out.println();

            while(result.next()){
                for (int i = 1; i<=cols; i++){
                    System.out.printf("%-25s |", result.getString(i));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void localizarEstado(Connection conn, String uf) {
        try{
            var sql = "select * from estado where uf = ?";
            var statement = conn.prepareStatement(sql);
            System.out.println(sql);
            statement.setString(1, uf);
            var result = statement.executeQuery();
            if(result.next()){
                System.out.printf("ID: %d Nome: %s UF: %s\n", result.getInt("id"), result.getString("nome"), result.getString("uf"));
            }
        } catch(SQLException e){
            System.err.println("erro ao executar consulta");
        }

    }
    private void listarEstados(Connection conn) {
        try {
            System.out.println("conexao realizada com sucesso");

            var statement = conn.createStatement();
            var result = statement.executeQuery("select * from estado");
            while(result.next()){
                System.out.printf("ID: %d Nome: %s UF: %s\n", result.getInt("id"), result.getString("nome"), result.getString("uf"));
            }
        } catch (SQLException e){
            System.err.println("nao foi possivel carregar"+ e.getMessage());
        }
        }
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL,USERNAME, PASSWORD);
    }

    /*private void carregarDriverJDBC() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("nao foi possível carregar");
        }
    }*/
}
