package messengerserver;

import java.io.*;
import java.net.*;

public class Alice {
    public static void main(String[] args) throws IOException {
        String serverAddress = "localhost";
        int serverPort = 12345;

        Socket socket = new Socket(serverAddress, serverPort);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        String serverResponse;
        String userInput;

        // Read and respond to "Enter name:"
        serverResponse = in.readLine();
        System.out.println(serverResponse); // Enter name:
        userInput = stdIn.readLine();
        out.println(userInput);

        // Read and respond to "Enter password:"
        serverResponse = in.readLine();
        System.out.println(serverResponse); // Enter password:
        userInput = stdIn.readLine();
        out.println(userInput);

        // Read and display the login response from the server
        serverResponse = in.readLine();
        System.out.println("Server response: " + serverResponse);

        if (!serverResponse.startsWith("Login successful")) {
            System.out.println("login failure.");
            return;
        }

        // After login, handle normal chat interaction
        while ((userInput = stdIn.readLine()) != null) {
            out.println(userInput);
            serverResponse = in.readLine();
            System.out.println("Server response: " + serverResponse);
        }

        // Close resources
        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }
}
