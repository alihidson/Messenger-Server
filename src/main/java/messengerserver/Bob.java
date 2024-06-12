package messengerserver;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class Bob extends Application {
    private PrintWriter out;
    private BufferedReader in;
    private TextArea messagesArea;
    private TextField userInputField;
    private TextField nameField;
    private PasswordField passwordField;
    private Button loginButton;
    private VBox loginPane;
    private VBox chatPane;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Bob");

        // Setup login pane
        loginPane = new VBox(10);
        loginPane.setPadding(new Insets(10));
        loginPane.setStyle("-fx-background-color: rgb(255, 123, 70);");

        Label nameLabel = new Label("Enter Name:");
        nameLabel.setFont(new Font("Arial", 14));
        nameField = new TextField();
        nameField.setFont(new Font("Arial", 14));

        Label passwordLabel = new Label("Enter Password:");
        passwordLabel.setFont(new Font("Arial", 14));
        passwordField = new PasswordField();
        passwordField.setFont(new Font("Arial", 14));

        loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: rgb(9,39,154); -fx-text-fill: white;");
        loginButton.setFont(new Font("Arial", 14));
        loginButton.setOnAction(e -> login());

        loginPane.getChildren().addAll(nameLabel, nameField, passwordLabel, passwordField, loginButton);

        // Setup chat pane
        chatPane = new VBox(10);
        chatPane.setPadding(new Insets(10));
        chatPane.setStyle("-fx-background-color: rgb(140,12,12);");

        Label titleLabel = new Label("Bob's Messenger");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setTextFill(Color.rgb(66,155,227));
        chatPane.getChildren().add(titleLabel);

        messagesArea = new TextArea();
        messagesArea.setEditable(false);
        messagesArea.setWrapText(true);
        messagesArea.setStyle("-fx-control-inner-background: rgb(213,121,176); -fx-font-size: 14px;");
        chatPane.getChildren().add(messagesArea);

        HBox hbox = new HBox(10);
        userInputField = new TextField();
        userInputField.setPrefWidth(300);
        userInputField.setFont(new Font("Arial", 14));
        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: rgb(103,23,201); -fx-text-fill: rgb(255,255,255);");
        sendButton.setFont(new Font("Arial", 14));
        sendButton.setOnAction(e -> sendMessage());
        hbox.getChildren().addAll(userInputField, sendButton);
        chatPane.getChildren().add(hbox);

        primaryStage.setScene(new Scene(loginPane, 400, 300));
        primaryStage.show();

        new Thread(this::connectToServer).start();
    }

    private void connectToServer() {
        String serverAddress = "localhost";
        int serverPort = 12345;

        try {
            Socket socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            updateMessagesArea(in.readLine()); // Enter name:

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login() {
        String name = nameField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || password.isEmpty()) {
            updateMessagesArea("Name and password cannot be empty.");
            return;
        }

        out.println(name);
        try {
            updateMessagesArea(in.readLine()); // Enter password:
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.println(password);
        try {
            String serverResponse = in.readLine();
            updateMessagesArea(serverResponse); // Login response

            if (serverResponse.startsWith("Login successful")) {
                primaryStage.setScene(new Scene(chatPane, 500, 350));
                primaryStage.show();
            } else {
                updateMessagesArea("Login failed. Please try again.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = userInputField.getText();
        if (message != null && !message.trim().isEmpty()) {
            out.println("Bob : " + message);
            userInputField.clear();
        }
    }

    private void updateMessagesArea(String message) {
        messagesArea.appendText(message + "\n");
    }
}
