package whiskey.code.courses.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import whiskey.code.courses.service.UserStateService;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class UserStateServiceImpl implements UserStateService {

    private final ConcurrentHashMap<Long, Instant> states = new ConcurrentHashMap<>();

    public void setState(Long chatId) {
        states.put(chatId, Instant.now().plus(Duration.ofMinutes(10)));
    }

    public boolean isWaitingForSearch(Long chatId) {
        return states.containsKey(chatId);
    }

    public void clearState(Long chatId) {
        states.remove(chatId);
    }

    @Scheduled(fixedRate =  10 * 60 * 1000)
    public void clearExpiredSessions() {
        states.entrySet().removeIf(entry -> Instant.now().isAfter(entry.getValue()));
    }

}
