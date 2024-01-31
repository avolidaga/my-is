package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.repository.ShootingRepository;

@RequiredArgsConstructor
@Service
public class ShootingService {
    private final ShootingRepository shootingRepository;

    public Long insertShooting(String username, boolean isKronbars){
        return shootingRepository.insertShootingData(username,isKronbars);
    }
}
