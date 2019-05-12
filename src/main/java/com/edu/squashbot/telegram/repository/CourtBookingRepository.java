package com.edu.squashbot.telegram.repository;

import com.edu.squashbot.telegram.entity.CourtBooking;
import com.edu.squashbot.telegram.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CourtBookingRepository extends MongoRepository<CourtBooking, String> {
    List<CourtBooking> findAllByStartLessThanEqualAndFinishAfter(LocalDateTime start, LocalDateTime finish);

    List<CourtBooking> findAllByUserAndStartAfter(User user, LocalDateTime start);
}
