package ru.practicum.explorewithme.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.event.model.Hit;
import ru.practicum.explorewithme.event.model.ViewStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
public class RestClient {
    private static final String FALSE_STRING_VALUE = "false";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${stats-post.path}")
    private String hitPostPath;
    @Value("${stats-get.path}")
    private String hitGetPath;

    RestTemplate rest = new RestTemplate();

    public void postHit(Hit hit) {
        log.info("Отправление пакета на сервер статистики: {}", hit);
        rest.postForEntity(hitPostPath, hit, Hit.class);
    }

    public ViewStats getStats(int eventId, LocalDateTime start, LocalDateTime end) {
        log.info("Обращение к сервису статистики");
        ViewStats[] stats = rest.getForObject(
                String.format(
                        hitGetPath,
                        start.format(formatter),
                        end.format(formatter),
                        List.of(URLEncoder.encode("/events/" + eventId, StandardCharsets.UTF_8)),
                        FALSE_STRING_VALUE),
                ViewStats[].class
        );
        return stats != null ? stats[0] : null;
    }
}
