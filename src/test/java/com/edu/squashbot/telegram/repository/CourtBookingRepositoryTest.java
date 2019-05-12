package com.edu.squashbot.telegram.repository;

import com.edu.squashbot.telegram.entity.Court;
import com.edu.squashbot.telegram.entity.CourtBooking;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CourtBookingRepositoryTest {
    @Autowired
    private CourtRepository courtRepository;
    @Autowired
    private CourtBookingRepository courtBookingRepository;

    @Test
    public void findAllByStartBeforeAndFinishAfter() {
        LocalDateTime time = LocalDateTime.of(2019, Month.MAY, 12, 19, 0);
        List<String> bookedCourtIds = courtBookingRepository.findAllByStartLessThanEqualAndFinishAfter(time, time)
                .stream()
                .map(CourtBooking::getCourt)
                .map(Court::getId)
                .collect(Collectors.toList());
        courtRepository.findAllByIdNotIn(bookedCourtIds, PageRequest.of(0, 4));
        assertFalse(bookedCourtIds.isEmpty());
    }
}