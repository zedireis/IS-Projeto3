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

    public static void main(String[] args) {
        try {
            addManager();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
