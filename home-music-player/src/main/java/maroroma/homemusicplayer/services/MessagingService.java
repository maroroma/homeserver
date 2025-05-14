package maroroma.homemusicplayer.services;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.messaging.api.SimpleBroadcastNotification;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void broadCast(SimpleBroadcastNotification notification) {
        simpMessagingTemplate.convertAndSend("/topic/notifications", notification);
    }

}
