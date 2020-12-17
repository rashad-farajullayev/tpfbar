package com.example.notification.service;

import com.example.notification.entity.OnlineUser;
import com.example.notification.model.NotificationChannel;
import com.example.notification.model.NotificationRequest;
import com.example.notification.model.WebSocketNotification;
import com.example.notification.repository.OnlineUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.hamcrest.core.IsInstanceOf.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class NotificationServiceTest {

    public static final String PARAM_NOTIFICATION_TEST_CONTENT = "notification test content";
    public static final String PARAM_FROM_USER = "from-user";
    public static final String PARAM_TO_USER = "rfarajullayev";
    public static final String PARAM_TOPIC_NOTIFICATION = "/topic/notification";
    public static final String PARAM_TYPE_SUCCESS = "success";
    public static final String PARAM_EMAIL = "rashad.farajullayev@gmail.com";
    @Autowired
    private NotificationService notificationService;

    @MockBean
    SimpMessagingTemplate simpMessagingTemplate;

    @MockBean
    OnlineUserRepository onlineUserRepository;

    @Captor
    ArgumentCaptor<String> recipientCaptor;

    @Captor
    ArgumentCaptor<String> topicCaptor;

    @Captor
    ArgumentCaptor<WebSocketNotification> webSocketNotificationCaptor;

    private NotificationRequest message;

    @BeforeEach
    void setUp() {
        message = NotificationRequest
                .builder()
                .content(PARAM_NOTIFICATION_TEST_CONTENT)
                .from(PARAM_FROM_USER)
                .to(PARAM_TO_USER)
                .type(PARAM_TYPE_SUCCESS)
                .email(PARAM_EMAIL)
                .build();
    }

    @Test
    @DisplayName ("The service shall send web socket notification if redis has user logged in record")
    void sendNotificationOverWebSocket() {

        when(onlineUserRepository.findById(PARAM_TO_USER)).thenReturn(Optional.of(OnlineUser.builder()
                .email(PARAM_EMAIL)
                .userName(PARAM_TO_USER).build()));

        var response = notificationService.sendNotification(message);

        verify(simpMessagingTemplate).convertAndSendToUser(
                recipientCaptor.capture(),
                topicCaptor.capture(),
                webSocketNotificationCaptor.capture());

        assertTrue(response.isSuccessfullySent());
        assertEquals(NotificationChannel.WEB_SOCKET, response.getChannel());
        assertEquals(PARAM_TO_USER, recipientCaptor.getValue());
        assertEquals(PARAM_TOPIC_NOTIFICATION, topicCaptor.getValue());
        assertEquals(PARAM_NOTIFICATION_TEST_CONTENT, webSocketNotificationCaptor.getValue().getContent());
    }

    @Test
    @DisplayName ("The service shall send email notification if redis has user logged in record")
    void sendNotificationOverEmail() {

        when(onlineUserRepository.findById(PARAM_TO_USER)).thenReturn(Optional.empty());

        var response = notificationService.sendNotification(message);

        assertTrue(response.isSuccessfullySent());
        assertEquals(NotificationChannel.EMAIL, response.getChannel());
    }

    @Test
    @DisplayName ("The service shall throw MessagingException exception if sending web socket message timed out")
    void throwExceptionWhenTimedOut() {

        when(onlineUserRepository.findById(PARAM_TO_USER)).thenReturn(Optional.of(OnlineUser.builder()
                .email(PARAM_EMAIL)
                .userName(PARAM_TO_USER).build()));

        doThrow(MessagingException.class).when(simpMessagingTemplate)
                .convertAndSendToUser(anyString(), anyString(), webSocketNotificationCaptor.capture());

        var response = notificationService.sendNotification(message);

        assertFalse(response.isSuccessfullySent());
        assertEquals(NotificationChannel.NONE, response.getChannel());
    }
}