package com.agh.librario.Notifications;

import com.agh.librario.Reservation.Reservation;
import com.agh.librario.User.User;

public interface NotificationService {
    void sendNotificationAboutEndingTimeOfReturn(User user, Reservation reservation);
    void sendNotificationAboutEndingTimeOfPickUp(User user, Reservation reservation);
    void sendNotificationAboutPositiveReservation(User user, Reservation reservation);
}
