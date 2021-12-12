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
    String CREDIT_PER_CLIENT = "SELECT * FROM client_credits";
    String PAYMENT_PER_CLIENT = "SELECT * FROM client_payments";
    String CURRENT_BALANCE = "SELECT * FROM balance WHERE client_email = ?";
    String TOTAL_CREDITS = "SELECT SUM(amount) FROM client_credits";
    String TOTAL_PAYMENTS = "SELECT SUM(amount) FROM client_payments";
    String TOTAL_BALANCE = "SELECT SUM(amount) FROM balance";


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

    //public List<Client_credit> creditPerClient() throws SQLException {
    public List<String> creditPerClient() throws SQLException {

        //List<Client_credit> list = new ArrayList<Client_credit>();
        List<String> list = new ArrayList<String>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( CREDIT_PER_CLIENT);
        while ( rs.next() ){
            Double amount = rs.getDouble("amount");
            System.out.println("[WHILE]: " + amount);

            String client_email = rs.getString("client_email");
            System.out.println("[WHILE]: " + client_email);
            //Client_credit client_credit = new Client_credit(amount, client_email);
            String result = "Client: " + client_email + " | " + "Credits: " + amount;
            list.add(result);
            //list.add(client_credit);
        }

        rs.close();
        stmt.close();
        conn.close();
        return list;

    }

    public List<String> paymentPerClient() throws SQLException {

        //List<Client_credit> list = new ArrayList<Client_credit>();
        List<String> list = new ArrayList<String>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( PAYMENT_PER_CLIENT);
        while ( rs.next() ){
            Double amount = rs.getDouble("amount");
            System.out.println("[WHILE]: " + amount);

            String client_email = rs.getString("client_email");
            System.out.println("[WHILE]: " + client_email);
            //Client_credit client_credit = new Client_credit(amount, client_email);
            String result = "Client: " + client_email + " | " + "Payment: " + amount;
            list.add(result);
            //list.add(client_credit);
        }

        rs.close();
        stmt.close();
        conn.close();
        return list;

    }


    public String currentBalance(String email) throws SQLException {

        PreparedStatement ps = conn.prepareStatement(CURRENT_BALANCE);
        ps.setString(1, email);
        ResultSet  rs = ps.executeQuery();
        String result = "Cliente não encontrado";
        while ( rs.next() ){
            Double amount = rs.getDouble("amount");
            System.out.println("[WHILE]: " + amount);

            String client_email = rs.getString("client_email");
            System.out.println("[WHILE]: " + client_email);
            //Client_credit client_credit = new Client_credit(amount, client_email);
            result = "Client: " + client_email + " | " + "Balance: " + amount;
            //list.add(client_credit);
        }

        rs.close();
        ps.close();
        conn.close();
        System.out.println("RESULT: " + result);
        return result;

    }


    public String totalCredits() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( TOTAL_CREDITS);
        String result = "NO RECORDS";
        while ( rs.next() ){
            Double total = rs.getDouble("sum");
            System.out.println("[WHILE]: " + total);
            result = "TOTAL CREDITS: " + total;
        }

        rs.close();
        stmt.close();
        conn.close();
        return result;

    }

    public String totalPayments() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( TOTAL_PAYMENTS);
        String result = "NO RECORDS";
        while ( rs.next() ){
            Double total = rs.getDouble("sum");
            System.out.println("[WHILE]: " + total);
            result = "TOTAL PAYMENTS: " + total;
        }

        rs.close();
        stmt.close();
        conn.close();
        return result;

    }

    public String totalBalance() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( TOTAL_BALANCE);
        String result = "NO RECORDS";
        while ( rs.next() ){
            Double total = rs.getDouble("sum");
            System.out.println("[WHILE]: " + total);
            result = "TOTAL BALANCE: " + total;
        }

        rs.close();
        stmt.close();
        conn.close();
        return result;

    }










}