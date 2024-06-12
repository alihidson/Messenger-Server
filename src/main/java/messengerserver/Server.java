package messengerserver;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Server {
    private static HashMap<String, String> userCredentials = new HashMap<>();

    public static void main(String[] args) throws IOException {
        userCredentials.put("Alice", "alice1234");
        userCredentials.put("Bob", "bob1234");

        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Server started. Waiting for clients...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);

            Thread clientThread = new Thread(new ClientHandler(clientSocket));
            clientThread.start();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String name;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                out.println("Enter name:");
                name = in.readLine();
                System.out.println("Client's name: " + name);

                out.println("Enter password:");
                String password = in.readLine();
                System.out.println("Client's password: " + password);

                if (validateCredentials(name, password)) {
                    out.println("Login successful. Welcome, " + name + "!");
                } else {
                    out.println("Invalid credentials");
                }

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received from client: " + inputLine);
                    out.println("Echo: " + inputLine);
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean validateCredentials(String name, String password) {
            String storedPassword = userCredentials.get(name);
            return storedPassword != null && storedPassword.equals(password);
        }
    }
}

