package com.agh.librario.Reservation;

import com.agh.librario.Book.Book;
import com.agh.librario.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByReservationUser(User user);
    List<Reservation> findAllByExpiredDate(Date date);
    Reservation findReservationByReservationBookAndReservationUser(Book book, User user);
    Reservation findByReservationUserAndReservationBook(User user, Book book);
}
