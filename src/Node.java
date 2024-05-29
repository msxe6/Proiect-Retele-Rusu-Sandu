import java.io.*;
import java.net.*;
import java.util.*;

public class Node {
    private String name;
    private List<String> neighborAddresses;
    private Map<String, Service> services;
    private ServerSocket serverSocket;
    private Socket connection;

    public Node(String name, List<String> neighborAddresses, int port) throws IOException {
        this.name = name;
        this.neighborAddresses = neighborAddresses;
        this.services = new HashMap<>();
        this.serverSocket = new ServerSocket(port);
        System.out.println(name + " started, listening on port " + port);
    }

    public void addService(Service service) {
        services.put(service.getName(), service);
    }

    public void start() {
        new Thread(this::acceptConnections).start();
        connectToNeighbors();
    }

    private void acceptConnections() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection from " + socket.getRemoteSocketAddress());
                new Thread(() -> handleConnection(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnection(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            String command;
            while ((command = in.readLine()) != null) {
                System.out.println("Received command: " + command);
                String response = executeCommand(command);
                out.println(response);
                System.out.println("Sent response: " + response);
                // Trimite confirmarea execuției comenzii
                out.println("Command executed successfully");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String executeCommand(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 2) {
            return "Invalid command format. Use: <action> <service_name>";
        }

        String action = parts[0];
        String serviceName = parts[1];

        Service service = services.get(serviceName);
        if (service == null) {
            return "Service not found";
        }

        switch (action) {
            case "start":
                service.start();
                return "Service " + serviceName + " started";
            case "stop":
                service.stop();
                return "Service " + serviceName + " stopped";
            case "status":
                return "Service " + serviceName + " is " + (service.isRunning() ? "running" : "stopped");
            default:
                return "Invalid command";
        }
    }

    public void connectToNeighbors() {
        for (String address : neighborAddresses) {
            String[] parts = address.split(":");
            if (parts.length != 2) {
                System.out.println("Invalid address format: " + address);
                continue;
            }

            String host = parts[0];
            int port;

            try {
                port = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number: " + parts[1]);
                continue;
            }

            try {
                connection = new Socket(host, port);
                System.out.println(name + " connected to " + address);
                break;
            } catch (IOException e) {
                System.out.println(name + " failed to connect to " + address + ": " + e.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", neighborAddresses=" + neighborAddresses +
                ", services=" + services +
                ", serverSocket=" + serverSocket +
                ", connection=" + connection +
                '}';
    }
}
