package ui;

import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import model.IntakeCategory;

public class NewIntakeForm extends GridPane {
    private final IntakeTracker tracker;
    private final ComboBox<IntakeCategory> categoryComboBox;
    private final TextField customerNameField;
    private final TextField customerLastNameField;
    private final TextField companyField;
    private final TextField phoneField;
    private final TextField emailField;
    private final VBox categorySpecificFields;

    public NewIntakeForm(IntakeTracker tracker) {
        this.tracker = tracker;
        this.setPadding(new Insets(20));
        this.setHgap(10);
        this.setVgap(10);
        this.getStyleClass().add("intake-form");

        // Initialize form fields
        categoryComboBox = new ComboBox<>();
        customerNameField = new TextField();
        customerLastNameField = new TextField();
        companyField = new TextField();
        phoneField = new TextField();
        emailField = new TextField();
        categorySpecificFields = new VBox(10);

        setupForm();
    }

    private void setupForm() {
        // Add form title
        Label titleLabel = new Label("New Intake");
        titleLabel.getStyleClass().add("form-title");
        add(titleLabel, 0, 0, 2, 1);

        // Add basic fields
        add(new Label("Category:"), 0, 1);
        add(categoryComboBox, 1, 1);
        categoryComboBox.getItems().addAll(IntakeCategory.values());
        categoryComboBox.setOnAction(e -> updateCategoryFields());

        add(new Label("Customer Name:"), 0, 2);
        add(customerNameField, 1, 2);

        add(new Label("Customer Last Name:"), 0, 3);
        add(customerLastNameField, 1, 3);

        add(new Label("Company:"), 0, 4);
        add(companyField, 1, 4);

        add(new Label("Phone:"), 0, 5);
        add(phoneField, 1, 5);

        add(new Label("Email:"), 0, 6);
        add(emailField, 1, 6);

        // Add category specific fields container
        add(categorySpecificFields, 0, 7, 2, 1);

        // Add submit button
        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("submit-button");
        submitButton.setOnAction(e -> handleSubmit());
        add(submitButton, 1, 8);

        // Add field validation
        setupValidation();
    }

    private void updateCategoryFields() {
        categorySpecificFields.getChildren().clear();
        IntakeCategory selected = categoryComboBox.getValue();
        
        if (selected != null) {
            switch (selected) {
                case RNS -> addRNSFields();
                case NCR -> addNCRFields();
                case SCO -> addSCOFields();
                case PROJECT -> addProjectFields();
                case SERVICE -> addServiceFields();
            }
        }
    }

    private void addRNSFields() {
        TextField locationField = new TextField();
        TextField incNumberField = new TextField();
        TextArea techNeedsArea = new TextArea();
        
        categorySpecificFields.getChildren().addAll(
            new Label("Location:"),
            locationField,
            new Label("INC Number:"),
            incNumberField,
            new Label("Tech Needs:"),
            techNeedsArea
        );
    }

    private void setupValidation() {
        // Real-time validation
        phoneField.textProperty().addListener((obs, old, newValue) -> {
            ValidationResult result = InputValidator.validatePhoneNumber(newValue);
            updateFieldValidation(phoneField, result);
        });

        emailField.textProperty().addListener((obs, old, newValue) -> {
            ValidationResult result = InputValidator.validateEmail(newValue);
            updateFieldValidation(emailField, result);
        });
    }

    private void updateFieldValidation(TextField field, ValidationResult result) {
        if (result.isValid()) {
            field.getStyleClass().remove("invalid-field");
            field.getStyleClass().add("valid-field");
        } else {
            field.getStyleClass().remove("valid-field");
            field.getStyleClass().add("invalid-field");
        }
    }

    private void handleSubmit() {
        try {
            // Validate all fields
            if (!validateAllFields()) {
                showError("Please correct the invalid fields");
                return;
            }

            // Create intake data
            IntakeData data = collectFormData();
            tracker.saveIntake(data);

            // Show success message
            showSuccess("Intake saved successfully");
            clearForm();
        } catch (Exception e) {
            showError("Error saving intake: " + e.getMessage());
        }
    }
} 