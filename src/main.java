import processing.core.PApplet;
import java.util.ArrayList;

public class Main extends PApplet {
    private XBeeManager xbeeManager;
    private NodeDiscoveryManager nodeDiscoveryManager;
    private ArrayList<Switch> switches;
    private float lastNodeDiscovery;

    public void settings() {
        size(800, 230);
    }

    public void setup() {
        switches = new ArrayList<>();
        xbeeManager = new XBeeManager("/dev/tty.usbserial-A1000iMG");
        nodeDiscoveryManager = new NodeDiscoveryManager(xbeeManager);

        try {
            xbeeManager.openConnection();
        } catch (Exception e) {
            println(e);
            error = 1;
        }

        nodeDiscoveryManager.discoverNodes();
        lastNodeDiscovery = millis();
    }

    public void draw() {
        background(255);
        for (NodeDiscover node : nodeDiscoveryManager.getNodes()) {
            XBeeAddress64 address64 = node.getNodeAddress64();
            boolean foundIt = false;
            for (Switch s : switches) {
                if (s.getAddress().equals(address64)) {
                    foundIt = true;
                    break;
                }
            }

            if (!foundIt && switches.size() < 5) {
                switches.add(new Switch(address64, switches.size()));
            }
        }

        for (Switch s : switches) {
            s.render();
        }

        if (millis() - lastNodeDiscovery > 15 * 60 * 1000) {
            nodeDiscoveryManager.discoverNodes();
            lastNodeDiscovery = millis();
        }
    }

    public void mousePressed() {
        for (Switch s : switches) {
            s.toggleState(xbeeManager);
        }
    }

    public static void main(String[] args) {
        PApplet.main("Main");
    }
}
