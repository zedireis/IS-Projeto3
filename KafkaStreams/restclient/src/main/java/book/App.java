package book;

import com.google.gson.Gson;
import pojo.Manager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class App {
    private static void addManager() throws URISyntaxException {
        System.out.println("ADD MANAGER");

        System.out.println("Name: ");
        Scanner managerName = new Scanner(System.in);
        System.out.println("Email: ");
        Scanner managerEmail = new Scanner(System.in);

        String manName = managerName.nextLine();
        String manEmail = managerEmail.nextLine();

        System.out.println("OUTPUTS: " + manEmail + "    " + manName);
        if (!manName.isEmpty() && manName.length() != 0 && !manEmail.isEmpty() && manEmail.length() != 0) {

            javax.ws.rs.client.Client client = ClientBuilder.newClient();
            WebTarget target = client.target(new URI("http://wildfly:8080/web/rest/myservice/addManager"));

            Manager manager = new Manager(manName, manEmail);
            Gson gson = new Gson();
            String json = gson.toJson(manager);
            Entity input = Entity.entity(json, MediaType.APPLICATION_JSON);

            Response response = target.request().post(input);

            String value = response.readEntity(String.class);
            System.out.println("RESPONSE MANAGER: " + value);
            response.close();
        }
    }

    private static void addClient() throws URISyntaxException {
        System.out.println("ADD CLIENT");

        System.out.println("Name: ");
        Scanner clientName = new Scanner(System.in);
        System.out.println("Email: ");
        Scanner clientEmail = new Scanner(System.in);

        System.out.println("Manager Email: ");
        Scanner managerEmail = new Scanner(System.in);

        String cliName = clientName.nextLine();
        String cliEmail = clientEmail.nextLine();
        String cliManagerEmail = managerEmail.nextLine();

        System.out.println("OUTPUTS: " + cliName + "    " + cliEmail + "    " + cliManagerEmail);
        if (!cliName.isEmpty() && cliName.length() != 0 && !cliEmail.isEmpty() && cliEmail.length() != 0
                && !cliManagerEmail.isEmpty() && cliManagerEmail.length() != 0) {

            javax.ws.rs.client.Client client = ClientBuilder.newClient();
            WebTarget target = client.target(new URI("http://wildfly:8080/web/rest/myservice/addClient"));

            pojo.Client pojoClient = new pojo.Client(cliName,cliEmail, cliManagerEmail);
            Gson gson = new Gson();
            String json = gson.toJson(pojoClient);
            Entity input = Entity.entity(json, MediaType.APPLICATION_JSON);

            Response response = target.request().post(input);

            String value = response.readEntity(String.class);
            System.out.println("RESPONSE CLIENT: " + value);
            response.close();
        }
    }

    private static void addCurrency() throws URISyntaxException {
        System.out.println("ADD CURRENCY");

        System.out.println("Name: ");
        Scanner currencyName = new Scanner(System.in);
        System.out.println("Conversion: ");
        Scanner currencyConversion = new Scanner(System.in);

        String cuName = currencyName.nextLine();
        String cuConversion = currencyConversion.nextLine();


        System.out.println("OUTPUTS: " + cuName + "    " + cuConversion );

        if (!cuName.isEmpty() && cuName.length() != 0 && !cuConversion.isEmpty() && cuConversion.length() != 0) {


            double conversion = Double.parseDouble(cuConversion);
            javax.ws.rs.client.Client client = ClientBuilder.newClient();
            WebTarget target = client.target(new URI("http://wildfly:8080/web/rest/myservice/addCurrency"));

            pojo.Currency pojoCurrency = new pojo.Currency(cuName,conversion);
            Gson gson = new Gson();
            String json = gson.toJson(pojoCurrency);
            Entity input = Entity.entity(json, MediaType.APPLICATION_JSON);

            Response response = target.request().post(input);

            String value = response.readEntity(String.class);
            System.out.println("RESPONSE CURRENCY: " + value);
            response.close();
        }
    }

    public static void main(String[] args) {
        try {
            //addManager();
            //addClient();
            addCurrency();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
