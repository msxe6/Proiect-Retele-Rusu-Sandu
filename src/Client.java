import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Map<String, String> servicesCommands;

    public Client(String address, int port) throws IOException {
        System.out.println("Trying to connect to " + address + ":" + port);
        this.socket = new Socket(address, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.servicesCommands = new HashMap<>();
        System.out.println("Connected to " + address + ":" + port);
    }

    public void loadServicesCommands(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    servicesCommands.put(parts[0], parts[1] + "," + parts[2]);
                } else {
                    System.out.println("Invalid line in services file: " + line);
                }
            }
        }
    }

    public String sendCommand(String command) throws IOException {
        out.println(command);
        return in.readLine();
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter node address (IP or hostname): ");
        String address = scanner.nextLine();

        System.out.print("Enter node port: ");
        int port = Integer.parseInt(scanner.nextLine());

        Client client = null;
        try {
            client = new Client(address, port);

            System.out.print("Enter path to services file: ");
            String servicesFilePath = scanner.nextLine();
            client.loadServicesCommands(servicesFilePath);

            while (true) {
                System.out.print("Enter command (start|stop|status service_name): ");
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("exit")) {
                    break;
                }

                String response = client.sendCommand(command);
                System.out.println("Response: " + response);
                System.out.println("Waiting for confirmation...");
                String confirmation = client.in.readLine();
                System.out.println("Confirmation: " + confirmation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }
}
