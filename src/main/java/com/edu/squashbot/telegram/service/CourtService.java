package com.edu.squashbot.telegram.service;

import com.edu.squashbot.telegram.entity.Court;

public interface CourtService {
    Court registerNewCourt(String courtName);
}
