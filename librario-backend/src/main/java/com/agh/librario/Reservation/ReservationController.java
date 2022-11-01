package com.agh.librario.Reservation;

import com.agh.librario.Book.Book;
import com.agh.librario.Book.BookRepository;
import com.agh.librario.Notifications.NotificationService;
import com.agh.librario.User.User;
import com.agh.librario.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@RestController
public class ReservationController {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ReservationFunc reservationFunc;

    @Autowired
    NotificationService notificationService;

    @PostMapping("/rest/reservation/{userId}/{bookId}")
    public ResponseEntity<String> addReservation(@PathVariable Long userId, @PathVariable Long bookId) {
        if (userRepository.findById(userId).isPresent() && bookRepository.findById(bookId).isPresent()) {
            Book book = bookRepository.findById(bookId).get();
            User user = userRepository.findById(userId).get();
            //zakładam, że jeden użytkownik może tylko jedną sztukę zarezerwować
            if (book.getStockCount() != 0 && (reservationRepository.findReservationByReservationBookAndReservationUser(book, user) == null)) {
                reservationFunc.makeReservation(user, book);
                notificationService.sendNotificationAboutPositiveReservation(user,
                        reservationRepository.findReservationByReservationBookAndReservationUser(book, user));

                return ResponseEntity.status(HttpStatus.OK).body("The book was succesfully reserved");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The Book is already reserved");
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/rest/reservation/{reservationId}")
    @Transactional
    public HashMap<String, String> deleteReservation(@PathVariable Long reservationId) {
        HashMap<String, String> map = new HashMap<>();
        if(reservationRepository.findById(reservationId).isPresent()) {
            Reservation toDeleteReservation = reservationRepository.findById(reservationId).get();
            reservationFunc.deleteReservation(toDeleteReservation);
            map.put("coded", "200");
            map.put("message", "Deleted successfully");
            return map;
        } else {
            map.put("coded", "400");
            map.put("Message", "There is no reservation");
            return map;
        }
    }

    @GetMapping("/rest/reservation/one/{reservationId}")
    Reservation one(@PathVariable Long reservationId) {
        return reservationRepository.findById(reservationId).get();
    }



    @GetMapping("/rest/reservation")
    List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    @GetMapping("/rest/reservation/{userId}")
    List<ReservationSummary> allUserReservations(@PathVariable Long userId) {
        if(userRepository.findById(userId).isPresent()) {
            List<Reservation> reservationList = reservationRepository.findByReservationUser(userRepository.findById(userId).get());
            List<ReservationSummary> reservationSummaries = new LinkedList<>();
            for (Reservation reservation : reservationList) {
                ReservationSummary reservationSummary = new ReservationSummary();
                reservationSummary.setId(reservation.getId());
                reservationSummary.setBookTitle(reservation.getReservationBook().getName());
                reservationSummary.setAuthorName(reservation.getReservationBook().getAuthor());
                reservationSummary.setReservationDate(reservation.getReservationDate());
                reservationSummary.setExpiredDate(reservation.getExpiredDate());
                reservationSummaries.add(reservationSummary);
            }
            return reservationSummaries;
        } else {
            return null;
        }
    }

    @GetMapping("/rest/reservation/user/{username}")
    List<ReservationSummary> allUserReservations(@PathVariable String username) {
        if(userRepository.findByUsername(username) != null) {
            long userID = userRepository.findByUsername(username).getId();
            List<ReservationSummary> reservationList = allUserReservations(userID);
            return reservationList;
        } else {
            return null;
        }
    }
}