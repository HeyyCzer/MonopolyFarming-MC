package com.heyyczer.monopolyfarming.database;

import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    protected static Connection con;

    public static void open() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/monopoly", "root", "");

//            createTables();
        } catch (Exception e) {
            LoggerFactory.getLogger(ConnectionFactory.class).error("Houve um erro ao abrir a conexão com o servidor MySQL! Segue abaixo o código de erro:");
            e.printStackTrace();
        }
    }

//    public static void createTables() {
//        List<String> tables = Arrays.asList();
//
//        for (String table : tables) {
//            try (PreparedStatement stm = con.prepareStatement(table)) {
//                stm.executeUpdate();
//
//                String text = String.format("Verificando existência e criando tabela: %s", table.split("`")[1]);
//                LoggerFactory.getLogger(ConnectionFactory.class).info(text);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
