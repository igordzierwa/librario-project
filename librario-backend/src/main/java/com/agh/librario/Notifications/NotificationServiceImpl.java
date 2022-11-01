package com.agh.librario.Notifications;

import com.agh.librario.Book.Book;
import com.agh.librario.Mail.MailSender;
import com.agh.librario.Reservation.Reservation;
import com.agh.librario.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    private MailSender mailSender;

    @Override
    public void sendNotificationAboutEndingTimeOfReturn(User user, Reservation reservation) {
        String subject = "Twoja Biblioteka - koniec czasu na zwrot książki";
        Book b = reservation.getReservationBook();

        String reservationInfo = "\nTytuł książki: " + b.getName() +
                "\nAutor: " + b.getAuthor() +
                "\nData rezerwacji: " + reservation.getReservationDate().toString() +
                "\nData zwrotu: " + reservation.getExpiredDate().toString();

        String textToSend = "Witaj " + user.getName() +
                ", \nTwoja rezerwacja  właśnie się zakończyła:\n" + reservationInfo +
                "\nProsimy o niezwłoczne zwrócenie książki do biblioteki." +
                "\n\nTwoja Biblioteka" +
                "\n\nWiadomość wygenerowana automatycznie, proszę na nią nie odpowiadać";

        mailSender.sendEmail(user.getEmail(), subject, textToSend);
    }

    @Override
    public void sendNotificationAboutEndingTimeOfPickUp(User user, Reservation reservation) {
        String subject = "Twoja Biblioteka - koniec czasu na odebranie książki";
        Book b = reservation.getReservationBook();

        String reservationInfo = "\nTytuł książki: " + b.getName() +
                "\nAutor: " + b.getAuthor() +
                "\nData rezerwacji: " + reservation.getReservationDate().toString() +
                "\nData zwrotu: " + reservation.getExpiredDate().toString();

        String textToSend = "Witaj " + user.getName() +
                ", \nCzas na odebranie książki z rezerwacji  właśnie się zakończył - rezerwacja zostaje usunięta:\n"
                + reservationInfo +
                "\n\nTwoja Biblioteka" +
                "\n\nWiadomość wygenerowana automatycznie, proszę na nią nie odpowiadać";

        mailSender.sendEmail(user.getEmail(), subject, textToSend);
    }

    @Override
    public void sendNotificationAboutPositiveReservation(User user, Reservation reservation) {
        String subject = "Twoja Biblioteka - rezerwacja zakończona pomyślnie";
        Book b = reservation.getReservationBook();

        String reservationInfo = "\nTytuł książki: " + b.getName() +
                "\nAutor: " + b.getAuthor() +
                "\nData rezerwacji: " + reservation.getReservationDate().toString() +
                "\nData zwrotu: " + reservation.getExpiredDate().toString();

        String textToSend = "Witaj " + user.getName() +
                ", \nRezerwacja  przebiegła pomyślnie.\n" + reservationInfo +
                "\n\nTwoja Biblioteka" +
                "\n\nWiadomość wygenerowana automatycznie, proszę na nią nie odpowiadać";

        mailSender.sendEmail(user.getEmail(), subject, textToSend);
    }
}
