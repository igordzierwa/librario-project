package com.agh.librario.Reservation;

import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class ReservationSummary {
    private Long id;
    private String bookTitle;
    private String authorName;
    private String reservationDate;
    private String expiredDate;
    private Long userID;
    private String userName;
    private int numberOfReservations;

    public void setReservationDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.reservationDate = dateFormat.format(date);
    }

    public void setExpiredDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.expiredDate = dateFormat.format(date);
    }
}
