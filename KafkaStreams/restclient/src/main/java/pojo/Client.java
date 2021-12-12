package pojo;


public class Client{

    private String name;
    private String email;


    private String manager_email;


    public Client() {}


    public Client(String name, String email, String manager_email) {
        this.name = name;
        this.email = email;
        this.manager_email = manager_email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getManager_email() {
        return manager_email;
    }

    public void setManager_email(String manager_email) {
        this.manager_email = manager_email;
    }

    public String toString(){
        return "Name: " + this.name + " " + "email: " + this.email + "Manager email:" + this.manager_email + "\n";
    }
}