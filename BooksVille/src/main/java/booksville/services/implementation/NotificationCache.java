package booksville.services.implementation;

import booksville.entities.model.BookEntity;
import booksville.entities.model.Notification;
import booksville.infrastructure.exceptions.ApplicationException;
import booksville.payload.response.NotificationResponse;
import booksville.repositories.NotificationRepository;
import booksville.services.NotificationCacheService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationCache implements NotificationCacheService {
    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional(readOnly = true)
    @Cacheable("notification")
    public String getNotificationData(Long id) {
        return notificationRepository.findById(id).orElseThrow(
                () -> new ApplicationException("Not found")
        ).getMessage();
    }


    @Override
    @Transactional
    @CachePut(value = "notification", key = "#notification.id")
    public String updateCachedData(Notification notification) {

        return notification.getMessage();
    }
}