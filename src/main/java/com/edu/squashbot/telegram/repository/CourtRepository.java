package com.edu.squashbot.telegram.repository;

import com.edu.squashbot.telegram.entity.Court;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourtRepository extends MongoRepository<Court, String> {
    Slice<Court> findAllByIdNotIn(List<String> ids, Pageable pageable);
}
