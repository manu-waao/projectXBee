import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NetworkMonitor {
    private NodeManager nodeManager;
    private NetworkTester networkTester;
    private int pingInterval = 10000;

    public NetworkMonitor(NodeManager nodeManager, NetworkTester networkTester) {
        this.nodeManager = nodeManager;
        this.networkTester = networkTester;
    }

    public void startMonitoring() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                monitorNetwork();
            }
        }, 0, pingInterval);
    }

    private void monitorNetwork() {
        ArrayList<NodeDiscover> nodes = nodeManager.getNodes();
        for (NodeDiscover node : nodes) {
            XBeeAddress64 address = node.getNodeAddress64();
            boolean isReachable = networkTester.pingNode(address);
            if (!isReachable) {
                System.out.println("Node " + address + " is not reachable. Please check the connection.");
            }
        }
    }
}
