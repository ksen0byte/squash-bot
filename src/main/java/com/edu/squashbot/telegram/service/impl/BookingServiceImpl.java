package com.edu.squashbot.telegram.service.impl;

import com.edu.squashbot.telegram.entity.Court;
import com.edu.squashbot.telegram.entity.CourtBooking;
import com.edu.squashbot.telegram.entity.User;
import com.edu.squashbot.telegram.repository.CourtBookingRepository;
import com.edu.squashbot.telegram.repository.CourtRepository;
import com.edu.squashbot.telegram.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private CourtRepository courtRepository;
    @Autowired
    private CourtBookingRepository courtBookingRepository;

    @Override
    public Optional<Court> getCourtAvailableForBooking(LocalDateTime time) {
        List<String> bookedCourtIds = courtBookingRepository.findAllByStartLessThanEqualAndFinishAfter(time, time)
                .stream()
                .map(CourtBooking::getCourt)
                .map(Court::getId)
                .collect(Collectors.toList());
        return courtRepository.findAllByIdNotIn(bookedCourtIds, PageRequest.of(0, 4)).get().findAny();
    }

    @Override
    public Optional<CourtBooking> makeBooking(User user, LocalDateTime time) {
        Optional<CourtBooking> courtBookingOpt = getCourtAvailableForBooking(time)
                .map(court -> CourtBooking.builder()
                        .start(time)
                        .finish(time.plus(Duration.ofHours(1)))
                        .user(user)
                        .court(court)
                        .build());
        return courtBookingOpt.map(booking -> courtBookingRepository.save(booking));
    }

    @Override
    public List<CourtBooking> getBookingsForUser(User user) {
        return courtBookingRepository.findAllByUserAndStartAfter(user, now());
    }

    @Override
    public void removeBooking(String courtBookingId) {
        courtBookingRepository.deleteById(courtBookingId);
    }
}
