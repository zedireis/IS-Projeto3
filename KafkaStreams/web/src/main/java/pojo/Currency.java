package pojo;

public class Currency{

    private String name;
    private Double conversion;


    public Currency() {}


    public Currency(String name, Double conversion) {
        this.name = name;
        this.conversion = conversion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getConversion() {
        return conversion;
    }

    public void setConversion(Double conversion) {
        this.conversion = conversion;
    }

    public String toString(){
        return "Name: " + this.name + " " + "Conversion to EUR: " + this.conversion + "\n";
    }
}