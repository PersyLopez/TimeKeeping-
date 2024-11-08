package search;

import model.IntakeData;
import model.IntakeCategory;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IntakeSearcher {
    private final List<IntakeData> intakes;
    
    public IntakeSearcher(List<IntakeData> intakes) {
        this.intakes = intakes;
    }

    public List<IntakeData> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchTerm = keyword.toLowerCase().trim();
        
        return intakes.stream()
            .filter(intake -> matchesKeyword(intake, searchTerm))
            .collect(Collectors.toList());
    }

    private boolean matchesKeyword(IntakeData intake, String keyword) {
        return intake.customerName().toLowerCase().contains(keyword) ||
               intake.customerLastName().toLowerCase().contains(keyword) ||
               intake.company().toLowerCase().contains(keyword) ||
               intake.categorySpecificData().values().stream()
                   .anyMatch(value -> value.toLowerCase().contains(keyword));
    }

    public List<IntakeData> advancedSearch(SearchCriteria criteria) {
        return intakes.stream()
            .filter(buildPredicate(criteria))
            .collect(Collectors.toList());
    }

    private Predicate<IntakeData> buildPredicate(SearchCriteria criteria) {
        List<Predicate<IntakeData>> predicates = new ArrayList<>();
        
        if (criteria.getCategory() != null) {
            predicates.add(intake -> intake.category() == criteria.getCategory());
        }
        
        if (criteria.getDateRange() != null) {
            predicates.add(intake -> isWithinDateRange(intake, criteria.getDateRange()));
        }
        
        if (criteria.getWjid() != null) {
            predicates.add(intake -> 
                intake.categorySpecificData().getOrDefault("WJID", "")
                    .contains(criteria.getWjid()));
        }
        
        if (criteria.getInc() != null) {
            predicates.add(intake -> 
                intake.categorySpecificData().getOrDefault("INC", "")
                    .contains(criteria.getInc()));
        }
        
        return predicates.stream()
            .reduce(x -> true, Predicate::and);
    }

    private boolean isWithinDateRange(IntakeData intake, DateRange range) {
        return !intake.timestamp().isBefore(range.startDate()) &&
               !intake.timestamp().isAfter(range.endDate());
    }
} 