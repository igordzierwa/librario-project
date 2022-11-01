package com.agh.librario.Reservation;

import com.agh.librario.Book.Book;
import com.agh.librario.User.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //zakładam że jedna książka przypada na jeden obiekt rezerwacji
    //mogłoby być jeszcze //@OneToMany(mappedBy = "reservation") - wtedy kilka książek na raz możnaby rezerwować
    @OneToOne
    private Book reservationBook;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User reservationUser;

    private Date reservationDate;

    private Date expiredDate;

    public Reservation(){}

    public void incrementNumberOfReservations() {
        this.reservationUser.addOneReservation();
    }

    public void decrementNumberOfReservations() {
        this.reservationUser.subtractOneReservation();
    }

    public void incrementNumberOfBooks() {
        this.reservationBook.incrementStockCount();
    }

    public void decrementNumberOfBooks() {
        this.reservationBook.decrementStockCount();
    }
}
