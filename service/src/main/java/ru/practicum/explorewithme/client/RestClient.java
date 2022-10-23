package ru.practicum.explorewithme.client;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.event.model.Hit;
import ru.practicum.explorewithme.event.model.ViewStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class RestClient {
    public static final String HIT_PATH_VALUE = "http://stats:9090/hit";
    public static final String STATS_PATH_VALUE = "http://stats:9090/stats?start=%s&end=%s&uris=%s&unique=%s";
    public static final String FALSE_STRING_VALUE = "false";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    RestTemplate rest = new RestTemplate();

    public void postHit(Hit hit) {
        rest.postForEntity(HIT_PATH_VALUE, hit, Hit.class);
    }

    public ViewStats getStats(int eventId, LocalDateTime start, LocalDateTime end) {
        ViewStats[] stats = rest.getForObject(
                String.format(
                        STATS_PATH_VALUE,
                        start.format(formatter),
                        end.format(formatter),
                        List.of(URLEncoder.encode("/events/" + eventId, StandardCharsets.UTF_8)),
                        FALSE_STRING_VALUE),
                ViewStats[].class
        );
        return stats != null ? stats[0] : null;
    }
}
