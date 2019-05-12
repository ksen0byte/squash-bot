package com.edu.squashbot.telegram.service.impl;

import com.edu.squashbot.telegram.entity.Court;
import com.edu.squashbot.telegram.repository.CourtRepository;
import com.edu.squashbot.telegram.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtServiceImpl implements CourtService {
    @Autowired
    private CourtRepository repository;

    @Override
    public Court registerNewCourt(String courtName) {
        return repository.save(Court.builder().name(courtName).build());
    }
}
