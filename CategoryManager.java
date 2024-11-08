import java.time.Duration;
import java.util.*;

public class CategoryManager {
    private final Map<String, Duration> categoryDurations;

    public CategoryManager() {
        this.categoryDurations = new HashMap<>();
    }

    public void addDuration(String category, Duration duration) {
        categoryDurations.merge(category, duration, Duration::plus);
    }

    public String generateSummary() {
        StringBuilder summary = new StringBuilder("End of Shift Summary:\n");
        Duration totalDuration = Duration.ZERO;

        for (Map.Entry<String, Duration> entry : categoryDurations.entrySet()) {
            String category = entry.getKey();
            Duration categoryDuration = entry.getValue();
            totalDuration = totalDuration.plus(categoryDuration);

            summary.append(String.format("Category: %s, Total Time: %d minutes\n", 
                category, categoryDuration.toMinutes()));
        }

        summary.append(String.format("Total Shift Duration: %d minutes\n", 
            totalDuration.toMinutes()));

        return summary.toString();
    }

    public Map<String, Duration> getCategoryDurations() {
        return new HashMap<>(categoryDurations); // Return a copy for safety
    }
} 