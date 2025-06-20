package ru.se.info.tinder.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.se.info.tinder.service.SendNotificationService;

@Component
class SendScheduledNotificationJob {

    private final SendNotificationService sendNotificationService;

    public SendScheduledNotificationJob(SendNotificationService sendNotificationService) {
        this.sendNotificationService = sendNotificationService;
    }

    @Scheduled(cron = "${jobs-scheduling.notification-job.cron}")
    @SchedulerLock(
            name = "blacklist-job",
            lockAtLeastFor = "${jobs-scheduling.notification-job.lock.at-least-for}",
            lockAtMostFor = "${jobs-scheduling.notification-job.lock.at-most-for}"
    )
    public void invokeSendScheduledNotification(){
        sendNotificationService.sendScheduledNotification();
    }
}