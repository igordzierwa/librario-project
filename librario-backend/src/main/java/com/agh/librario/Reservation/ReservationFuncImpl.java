package com.agh.librario.Reservation;

import com.agh.librario.Book.Book;
import com.agh.librario.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class ReservationFuncImpl implements ReservationFunc{
    @Autowired
    private ReservationRepository reservationRepository;
    //zakładam że długość rezerwacji to np. 14 dni
    @Override
    public void makeReservation(User user, Book book) {
        Reservation reservation = new Reservation();

        Date reservationDate = new Date();
        Date expiredDate = setExpiredDate(reservationDate);

        reservation.setReservationUser(user);
        reservation.setReservationBook(book);
        reservation.setReservationDate(reservationDate);
        reservation.setExpiredDate(expiredDate);
        reservation.decrementNumberOfBooks();
        reservation.incrementNumberOfReservations();
        reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Reservation reservation) {
        reservation.incrementNumberOfBooks();
        reservation.decrementNumberOfReservations();
        reservationRepository.deleteById(reservation.getId());
    }

    public Date setExpiredDate(Date reservationDate) {
        //convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(reservationDate);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DATE, 14);
        Date expiredDate = c.getTime();
        return expiredDate;
    }
}
