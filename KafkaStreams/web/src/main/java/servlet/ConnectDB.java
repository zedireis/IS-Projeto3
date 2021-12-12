package servlet;

import pojo.Client;
import pojo.Currency;
import pojo.Manager;

import javax.ws.rs.core.GenericEntity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectDB {

    private Connection conn;


    String INSERT_MANAGER = "INSERT INTO manager (email, nome) VALUES (?, ?)";
    String INSERT_CLIENT = "INSERT INTO client (email, nome, manager_email) VALUES (?, ?, ?)";
    String INSERT_CURRENCY = "INSERT INTO currency (name, conversion) VALUES (?, ?)";
    String LIST_MANAGERS = "SELECT * FROM manager";
    String LIST_CLIENTS = "SELECT * FROM client";
    String LIST_CURRENCIES = "SELECT * FROM currency";


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

    public void addClient(String email, String nome,String manager_email) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_CLIENT);
        ps.setString(1, email);
        ps.setString(2, nome);
        ps.setString(3, manager_email);
        ps.executeUpdate();
        ps.close();
    }

    public void addCurrency(String name, Double conversion) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_CURRENCY);
        ps.setString(1, name);
        ps.setDouble(2, conversion);
        ps.executeUpdate();
        ps.close();
    }

    public List<Manager> listManagers() throws SQLException {
        List<Manager> list = new ArrayList<Manager>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( LIST_MANAGERS);
        System.out.println("ANTES DO WHILE");
        while ( rs.next() ){
            System.out.println("ENTREI NO WHILE");
            String email = rs.getString("email");
            System.out.println("[WHILE]: " + email);
            String name = rs.getString("nome");
            System.out.println("[WHILE]: " + name);
            Manager manager = new Manager(name, email);
            list.add(manager);
        }

        rs.close();
        stmt.close();
        conn.close();
        return list;

    }

    public List<Client> listClients() throws SQLException {
        List<Client> list = new ArrayList<Client>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( LIST_CLIENTS);
        System.out.println("ANTES DO WHILE");
        while ( rs.next() ){
            System.out.println("ENTREI NO WHILE");
            String email = rs.getString("email");
            System.out.println("[WHILE]: " + email);
            String name = rs.getString("nome");
            System.out.println("[WHILE]: " + name);
            String manager_email = rs.getString("manager_email");
            System.out.println("[WHILE]: " + manager_email);
            Client client = new Client(name, email, manager_email);
            list.add(client);
        }

        rs.close();
        stmt.close();
        conn.close();
        return list;

    }

    public List<Currency> listCurrencies() throws SQLException {
        List<Currency> list = new ArrayList<Currency>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( LIST_CURRENCIES);
        System.out.println("ANTES DO WHILE");
        while ( rs.next() ){
            System.out.println("ENTREI NO WHILE");
            String name = rs.getString("name");
            System.out.println("[WHILE]: " + name);

            Double conversion = rs.getDouble("conversion");
            System.out.println("[WHILE]: " + conversion);
            Currency currency = new Currency(name, conversion);
            list.add(currency);
        }

        rs.close();
        stmt.close();
        conn.close();
        return list;

    }
}