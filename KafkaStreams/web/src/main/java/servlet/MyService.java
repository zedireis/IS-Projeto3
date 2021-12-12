package servlet;

import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import pojo.*;

@Path("/myservice")
@Produces(MediaType.APPLICATION_JSON)
public class MyService {

    private ConnectDB db = new ConnectDB();

    @POST
    @Path("/addManager")
    @Consumes(MediaType.APPLICATION_JSON)
    public String method1(Manager manager) {
        try {
            db.addManager(manager.getEmail(), manager.getName());
            return "Adicionado";
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "Falhou";
    }

    @POST
    @Path("/addClient")
    public String methodAddClient(Client client) {
        try {
            db.addClient(client.getName(), client.getEmail(), client.getManager_email());
            return "Cliente Adicionado";
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "Cliente Falhou";
    }

    @POST
    @Path("/addCurrency")
    public String methodAddCurrency(Currency currency) {
        try {
            System.out.println("[MY SERVICE]: " + currency.getName() + "    "  + currency.getConversion());
            db.addCurrency(currency.getName(), currency.getConversion());
            return "Currency Adicionado";
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "Currency Falhou";
    }
    @POST
    @Path("/person4")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response method4(Person person) {
        System.out.println("M4 executing....");
        String str = "Person received : " + person.getName() + " " + person.getAge();
        return Response.status(Status.OK).entity(str).build();
    }
    @POST
    @Path("/person5")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response method5(Person person) {
        System.out.println("M5 executing....");
        person.setName("No Name");
        person.setAge(person.getAge() + 1);
        return Response.status(Status.OK).entity(person).build();
    }
    /*@GET
    @Path("/person6")
    @Produces(MediaType.APPLICATION_JSON)
    public Response method6() {
        System.out.println("M6 executing....");
        Person p1 = new Person("James", 60);
        Person p2 = new Person("Kate", 61);
        Person p3 = new Person("Claire", 62);
        List<Person> personList = List.of(p1, p2, p3);
        return Response.ok().entity(personList).build();
    }*/
}