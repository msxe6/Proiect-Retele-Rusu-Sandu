import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Configurarea nodurilor cu porturi și adrese de vecini
            Node node1 = new Node("Node1", Arrays.asList("localhost:5001", "localhost:5002"), 5000);
            Node node2 = new Node("Node2", Arrays.asList("localhost:5000", "localhost:5002"), 5001);
            Node node3 = new Node("Node3", Arrays.asList("localhost:5000", "localhost:5001"), 5002);

            List<Node> listaNoduri = new ArrayList<Node>();
            listaNoduri.add(node1);
            listaNoduri.add(node2);
            listaNoduri.add(node3);

            // Adăugarea serviciilor la fiecare nod
            node1.addService(new Service("ServiceA"));
            node1.addService(new Service("ServiceB"));

            node2.addService(new Service("ServiceA"));
            node2.addService(new Service("ServiceC"));

            node3.addService(new Service("ServiceB"));
            node3.addService(new Service("ServiceC"));

            // Pornirea fiecărui nod
            node1.start();
            node2.start();
            node3.start();
            listaNoduri.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
