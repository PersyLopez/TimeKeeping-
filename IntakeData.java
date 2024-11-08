public record IntakeData(
    String name,
    String lastName,
    String company,
    String phoneNumber,
    String email,
    String category,
    Map<String, String> categorySpecificData
) {} 