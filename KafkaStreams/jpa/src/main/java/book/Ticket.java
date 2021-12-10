/*
package book;

import javax.persistence.*;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private Trip trip;

    public Ticket() { }

    public Ticket(Passenger p, Trip t) {
        this.passenger=p;
        this.trip=t;
    }

    public Trip getTrip() {
        return trip;
    }

    public  Passenger getPassenger(){
        return passenger;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}*/
