package com.edu.squashbot.telegram.service;

import com.edu.squashbot.telegram.entity.Court;
import com.edu.squashbot.telegram.entity.CourtBooking;
import com.edu.squashbot.telegram.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    Optional<Court> getCourtAvailableForBooking(LocalDateTime time);

    Optional<CourtBooking> makeBooking(User user, LocalDateTime time);

    List<CourtBooking> getBookingsForUser(User user);

    void removeBooking(String courtBookingId);
}
