package book;

import com.google.gson.Gson;
import pojo.Manager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;

import javax.sound.midi.Soundbank;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

public class App {
    private static void options() throws URISyntaxException {


        while(true){
            System.out.println("1. Add managers to the database");
            System.out.println("2. Add clients to the database");
            System.out.println("3. Add a currency and respective exchange rate for the euro to the database.");
            System.out.println("4. List managers from the database");
            System.out.println("5. List clients from the database.");
            System.out.println("6. List currencies.");
            System.out.println("7. Get the credit per client");
            System.out.println("8. Get the payments per client.");
            System.out.println("9. Get the current balance of a client");
            System.out.println("10. Get the total credits");
            System.out.println("11. Get the total payments");
            System.out.println("12. Get the total balance");
            System.out.println("13. Compute the bill for each client for the last month");
            System.out.println("14. Get the list of clients without payments for the last two months.");
            System.out.println("15. Get the data of the person with the highest outstanding debt ");
            System.out.println("16. Get the data of the manager who has made the highest revenue in payments from his or her clients.");

            Scanner obj  = new Scanner(System.in);
            System.out.println("\nCHOOSE OPTION");

            String option = obj.nextLine();
            int op = 0;
            try
            {
                op = Integer.parseInt(option);
            }
            catch (NumberFormatException e)
            {}

            switch(op) {
                case 1:
                    addManager();
                    break;
                case 2:
                    addClient();
                    break;
                case 3:
                    addCurrency();
                    break;
                case 4:
                    listManagers();
                case 5:
                    listClients();
                    break;
                case 6:
                    listCurrencies();
                    break;
                case 7:
                    getCreditperClient();
                    break;
                case 8:
                    getPaymentsperClient();
                    break;
                case 9:
                    currentBalance();
                    break;
                case 10:
                    getTotalCredits();
                    break;
                case 11:
                    getTotalPayments();
                    break;
                case 12:
                    getTotalBalance();
                    break;
                case 15:
                    getMostNegativeCurrentBalance();
                    break;






                default:
                    // code block

            }
        }



    }

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

    private static void listManagers() throws URISyntaxException{
        System.out.println("LIST MANAGERS");
        javax.ws.rs.client.Client client = ClientBuilder.newClient();
        WebTarget target = client.target(new URI("http://wildfly:8080/web/rest/myservice/listManagers"));

        Response response = target.request().get();

        List<Manager> result = response.readEntity(new GenericType<List<Manager>>() {});


        for(int i = 0; i < result.size(); i++){
            System.out.println(result.get(i).toString());
        }
        response.close();
    }

    private static void listClients() throws URISyntaxException{
        System.out.println("LIST CLIENTS");
        javax.ws.rs.client.Client client = ClientBuilder.newClient();
        WebTarget target = client.target(new URI("http://wildfly:8080/web/rest/myservice/listClients"));

        Response response = target.request().get();
        List<pojo.Client> result = response.readEntity(new GenericType<List<pojo.Client>>() {});

        for(int i = 0; i < result.size(); i++){
            System.out.println(result.get(i).toString());
        }
        response.close();
    }

    private static void listCurrencies() throws URISyntaxException{
        System.out.println("LIST CURRENCIES");
        javax.ws.rs.client.Client client = ClientBuilder.newClient();
        WebTarget target = client.target(new URI("http://wildfly:8080/web/rest/myservice/listCurrencies"));

        Response response = target.request().get();
        List<pojo.Currency> result = response.readEntity(new GenericType<List<pojo.Currency>>() {});

        for(int i = 0; i < result.size(); i++){
            System.out.println(result.get(i).toString());
        }
        System.out.println("\n------------------------------------------------\n\n");
        response.close();
    }

    private static void getCreditperClient() throws URISyntaxException{
        System.out.println("CREDIT PER CLIENT\n");
        javax.ws.rs.client.Client client = ClientBuilder.newClient();
        WebTarget target = client.target(new URI("http://wildfly:8080/web/rest/myservice/creditPerClient"));

        Response response = target.request().get();
        //List<pojo.Client_credit> result = response.readEntity(new GenericType<List<pojo.Client_credit>>() {});

        List<String> result = response.readEntity(new GenericType<List<String>>() {});

        for(int i = 0; i < result.size(); i++){
            //System.out.println(result.get(i).toString());
            System.out.println(result.get(i));

        }
        System.out.println("\n------------------------------------------------\n\n");
        response.close();
    }

    private static void getPaymentsperClient() throws URISyntaxException{
        System.out.println("PAYMENT PER CLIENT\n");
        javax.ws.rs.client.Client client = ClientBuilder.newClient();
        WebTarget target = client.target(new URI("http://wildfly:8080/web/rest/myservice/paymentPerClient"));

        Response response = target.request().get();
        //List<pojo.Client_credit> result = response.readEntity(new GenericType<List<pojo.Client_credit>>() {});

        List<String> result = response.readEntity(new GenericType<List<String>>() {});

        for(int i = 0; i < result.size(); i++){
            //System.out.println(result.get(i).toString());
            System.out.println(result.get(i));

        }
        System.out.println("\n------------------------------------------------\n\n");
        response.close();
    }

    private static void currentBalance() throws URISyntaxException{
        System.out.println("CURRENT BALANCE\n");
        javax.ws.rs.client.Client client = ClientBuilder.newClient();
        WebTarget target = client.target(new URI("http://wildfly:8080/web/rest/myservice/currentBalance"));

        System.out.println("Email do cliente: ");
        Scanner clientEmail = new Scanner(System.in);

        String cliEmail= clientEmail.nextLine();

        target = target.queryParam("email", cliEmail);
        Response response = target.request().get();

        //List<pojo.Client_credit> result = response.readEntity(new GenericType<List<pojo.Client_credit>>() {});

        String result = response.readEntity(new GenericType<String>() {});

        System.out.println(result);
        System.out.println("\n------------------------------------------------\n\n");
        response.close();
    }


    private static void getTotalCredits() throws URISyntaxException{
        System.out.println("GET TOTAL CREDITS\n");
        javax.ws.rs.client.Client client = ClientBuilder.newClient();
        WebTarget target = client.target(new URI("http://wildfly:8080/web/rest/myservice/getTotalCredits"));

        Response response = target.request().get();
        //List<pojo.Client_credit> result = response.readEntity(new GenericType<List<pojo.Client_credit>>() {});

        String result = response.readEntity(new GenericType<String>() {});

        System.out.println(result);
        System.out.println("\n------------------------------------------------\n\n");
        response.close();
    }

    private static void getTotalPayments() throws URISyntaxException{
        System.out.println("GET TOTAL CREDITS\n");
        javax.ws.rs.client.Client client = ClientBuilder.newClient();
        WebTarget target = client.target(new URI("http://wildfly:8080/web/rest/myservice/getTotalPayments"));

        Response response = target.request().get();
        //List<pojo.Client_credit> result = response.readEntity(new GenericType<List<pojo.Client_credit>>() {});

        String result = response.readEntity(new GenericType<String>() {});

        System.out.println(result);
        System.out.println("\n------------------------------------------------\n\n");
        response.close();
    }

    private static void getTotalBalance() throws URISyntaxException{
        System.out.println("GET TOTAL BALANCE\n");
        javax.ws.rs.client.Client client = ClientBuilder.newClient();
        WebTarget target = client.target(new URI("http://wildfly:8080/web/rest/myservice/getTotalBalance"));

        Response response = target.request().get();
        //List<pojo.Client_credit> result = response.readEntity(new GenericType<List<pojo.Client_credit>>() {});

        String result = response.readEntity(new GenericType<String>() {});

        System.out.println(result);
        System.out.println("\n------------------------------------------------\n\n");
        response.close();
    }


    private static void getMostNegativeCurrentBalance() throws URISyntaxException{
        System.out.println("HIGHEST OUTSTANDING DEBT\n");
        javax.ws.rs.client.Client client = ClientBuilder.newClient();
        WebTarget target = client.target(new URI("http://wildfly:8080/web/rest/myservice/getMostNegativeCurrentBalance"));

        Response response = target.request().get();
        List<pojo.Client> result = response.readEntity(new GenericType<List<pojo.Client>>() {});

        for(int i = 0; i < result.size(); i++){
            System.out.println(result.get(i).toString());
        }
        response.close();
        System.out.println("\n------------------------------------------------\n\n");
        response.close();
    }




    public static void main(String[] args) {
        try {

            options();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
