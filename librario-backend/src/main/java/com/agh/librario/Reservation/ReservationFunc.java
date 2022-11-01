package com.agh.librario.Reservation;

import com.agh.librario.Book.Book;
import com.agh.librario.User.User;

public interface ReservationFunc {
    void makeReservation(User user, Book book);
    void deleteReservation(Reservation reservation);
}
