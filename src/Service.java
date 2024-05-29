import java.util.HashMap;
import java.util.Map;

public class Service {
    private String name;
    private boolean running;

    public Service(String name) {
        this.name = name;
        this.running = false;
    }

    public String getName() {
        return name;
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        running = true;
        System.out.println("Service " + name + " started.");
    }

    public void stop() {
        running = false;
        System.out.println("Service " + name + " stopped.");
    }
}
