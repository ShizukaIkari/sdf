/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.trabalhosd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Serenna
 */
public class ConnectPostgres {
    public String url = "jdbc:postgresql://tuffi.db.elephantsql.com:5432/jqmltnoq";
    public String username = "jqmltnoq";
    public String password = "pWYbeG6Ij3zF6mJwxbhjxs0auts9Xhz3";
    private ResultSet rs;
    private Statement st;
    public static Connection db;
    
    public ConnectPostgres(){
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        
        try {
            db = DriverManager.getConnection(this.url,this.username, this.password);
        } 
        catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public ConnectPostgres(String url, String user, String pass){
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        this.url = url;
        this.username = user;
        this.password = pass;
        try {
            db = DriverManager.getConnection(this.url,this.username, this.password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void closeConnection(){
        try {
            this.db.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void closeStatement() {
        try {
            this.st.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void closeResultSet() {
        try {
            this.rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static Connection getDb() {
        return db;
    }

    public static void setDb(Connection db) {
        ConnectPostgres.db = db;
    }
    //Atualiza coisas no banco
    public void executeSql(String query) {
        try {
            st = db.createStatement();
            //executa o sql no meu banco de dados
            st.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    //Pega dados do banco raw
    public ArrayList retrieveRawData(String query){
        ArrayList result = new ArrayList();
        ArrayList linhas;
        
        try {
            st = db.createStatement();
            rs = st.executeQuery(query);
            //rs é um conjunto de linhas 
            while (rs.next()) {
                linhas = new ArrayList();
                linhas.add(rs.getString("coletado"));
                linhas.add(rs.getString("temperatura"));
                linhas.add(rs.getString("umidade")); //getString retorna valor da coluna
                linhas.add(rs.getString("pressao"));
                result.add(linhas);
            }
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    
    public ArrayList selectUniqData(){
        ArrayList result = new ArrayList();
        String query = "SELECT DISTINCT data FROM dados";
        try {
            st = db.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
     //Retorna o que é pedido na query
    public HashMap selectValidData(String medida, String data){
        String query = "SELECT "+medida+" FROM dados WHERE data = '"+data+"';";
        HashMap result = new HashMap();
        ArrayList medicoes = new ArrayList(); 
        
        try {
            st = db.createStatement();
            rs = st.executeQuery(query);
            
            //rs é um conjunto de linhas 
            
            while (rs.next()) {
                medicoes.add(rs.getString(1));
                result.put(data,medicoes);
            }
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

}
