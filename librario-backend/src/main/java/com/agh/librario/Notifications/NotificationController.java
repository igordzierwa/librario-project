package com.agh.librario.Notifications;

import com.agh.librario.Book.Book;
import com.agh.librario.Reservation.Reservation;
import com.agh.librario.Reservation.ReservationRepository;
import com.agh.librario.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Scheduled(cron = "0 00 12 * * ?")
    public void findEndingReservations(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date date = c.getTime();
        List<Reservation> endedReservations = reservationRepository.findAllByExpiredDate(date);
        endedReservations.forEach(r -> {
            User user = r.getReservationUser();
            notificationService.sendNotificationAboutEndingTimeOfReturn(user, r);
        });
    }
}
