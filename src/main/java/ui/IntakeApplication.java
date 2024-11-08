package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class IntakeApplication extends Application {
    private IntakeTracker tracker;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.tracker = new IntakeTracker();

        primaryStage.setTitle("Intake Tracker");
        
        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setLeft(createNavigationMenu());
        mainLayout.setCenter(createWelcomeScreen());

        Scene scene = new Scene(mainLayout, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createNavigationMenu() {
        VBox navigation = new VBox(10);
        navigation.setPadding(new Insets(10));
        navigation.getStyleClass().add("navigation-menu");

        Button newIntakeBtn = new Button("New Intake");
        Button searchBtn = new Button("Search Records");
        Button reportsBtn = new Button("Reports");
        Button exportBtn = new Button("Export Data");
        Button settingsBtn = new Button("Settings");

        // Style buttons
        newIntakeBtn.getStyleClass().add("nav-button");
        searchBtn.getStyleClass().add("nav-button");
        reportsBtn.getStyleClass().add("nav-button");
        exportBtn.getStyleClass().add("nav-button");
        settingsBtn.getStyleClass().add("nav-button");

        // Add button actions
        newIntakeBtn.setOnAction(e -> showNewIntakeForm());
        searchBtn.setOnAction(e -> showSearchScreen());
        reportsBtn.setOnAction(e -> showReportsScreen());
        exportBtn.setOnAction(e -> showExportScreen());
        settingsBtn.setOnAction(e -> showSettingsScreen());

        navigation.getChildren().addAll(
            newIntakeBtn, searchBtn, reportsBtn, exportBtn, settingsBtn
        );

        return navigation;
    }

    private void showNewIntakeForm() {
        NewIntakeForm intakeForm = new NewIntakeForm(tracker);
        setMainContent(intakeForm);
    }

    private void showSearchScreen() {
        SearchScreen searchScreen = new SearchScreen(tracker);
        setMainContent(searchScreen);
    }

    private void setMainContent(javafx.scene.Node content) {
        BorderPane mainLayout = (BorderPane) primaryStage.getScene().getRoot();
        mainLayout.setCenter(content);
    }

    public static void main(String[] args) {
        launch(args);
    }
} 