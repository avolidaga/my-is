package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.NotificationMessage;
import ru.se.info.tinder.entity.Notification;
import ru.se.info.tinder.repository.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SendNotificationService {

    private final NotificationRepository notificationRepository;

    @Value("${send-notification-job.send-before-hours}")
    private final int sendBeforeHours;

    private final EmailService emailService;

    public void sendScheduledNotification() {
        List<Notification> notifications = notificationRepository.findAllScheduledNotification(sendBeforeHours);

        notifications.forEach(notification -> {
            emailService.send(notification.getContent(), notification.getEmail());
        });
    }

    public void saveNotification(NotificationMessage notification) {
        notificationRepository.save(new Notification(notification.getUserId(), notification.getEmail(), notification.getContent()));
    }

    public void sendNotification(NotificationMessage notification) {
        emailService.send(notification.getContent(), notification.getEmail());
    }
}
