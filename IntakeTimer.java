import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class IntakeTimer {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;

    public void startTimer() {
        startTime = LocalDateTime.now();
    }

    public void endTimer() {
        endTime = LocalDateTime.now();
        duration = Duration.between(startTime, endTime);
    }

    public String getTimerDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Start: %s, End: %s, Duration: %d minutes",
                startTime.format(formatter),
                endTime.format(formatter),
                duration.toMinutes());
    }

    public Duration getDuration() {
        return duration;
    }
} 