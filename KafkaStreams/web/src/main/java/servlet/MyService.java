package servlet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
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
    @GET
    @Path("/listManagers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response method4() throws SQLException {
        System.out.println("ENTROU NO METHOD LIST MANAGERS");
        List<Manager> list = db.listManagers();

        GenericEntity<List<Manager>> entities = new GenericEntity<List<Manager>>(list){};

        return Response.ok().entity(list).build();
    }

    @GET
    @Path("/listClients")
    @Produces(MediaType.APPLICATION_JSON)
    public Response method5() throws SQLException {
        List<Client> list = db.listClients();

        //GenericEntity<List<Client>> entities = new GenericEntity<List<Client>>(list){};

        return Response.ok().entity(list).build();
    }

    @GET
    @Path("/listCurrencies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response method6() throws SQLException {
        List<Currency> list = db.listCurrencies();

        //GenericEntity<List<Client>> entities = new GenericEntity<List<Client>>(list){};

        return Response.ok().entity(list).build();
    }

    @GET
    @Path("/creditPerClient")
    @Produces(MediaType.APPLICATION_JSON)
    public Response method7() throws SQLException {
        // List<Client_credit> list = db.creditPerClient();
        List<String> list = db.creditPerClient();


        return Response.ok().entity(list).build();
    }

    @GET
    @Path("/paymentPerClient")
    @Produces(MediaType.APPLICATION_JSON)
    public Response method8() throws SQLException {
        // List<Client_credit> list = db.creditPerClient();
        List<String> list = db.paymentPerClient();


        return Response.ok().entity(list).build();
    }

    @GET
    @Path("/currentBalance")
    //@Produces(MediaType.APPLICATION_JSON)
    public Response method9(@QueryParam("email") String email) throws SQLException {
        // List<Client_credit> list = db.creditPerClient();
        String result = db.currentBalance(email);


        return Response.ok().entity(result).build();
    }

    @GET
    @Path("/getTotalCredits")
    //@Produces(MediaType.APPLICATION_JSON)
    public Response method10() throws SQLException {
        // List<Client_credit> list = db.creditPerClient();
        String result = db.totalCredits();


        return Response.ok().entity(result).build();
    }

    @GET
    @Path("/getTotalPayments")
    //@Produces(MediaType.APPLICATION_JSON)
    public Response method11() throws SQLException {
        // List<Client_credit> list = db.creditPerClient();
        String result = db.totalPayments();


        return Response.ok().entity(result).build();
    }

    @GET
    @Path("/getTotalBalance")
    //@Produces(MediaType.APPLICATION_JSON)
    public Response method12() throws SQLException {
        // List<Client_credit> list = db.creditPerClient();
        String result = db.totalBalance();


        return Response.ok().entity(result).build();
    }








}