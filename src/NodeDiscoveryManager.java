import java.util.ArrayList;
import com.rapplogic.xbee.api.zingbee.NodeDiscover;
import com.rapplogic.xbee.api.XBeeAddress64;

public class NodeDiscoveryManager {
    private XBeeManager xbeeManager;
    private ArrayList<NodeDiscover> nodes;

    public NodeDiscoveryManager(XBeeManager xbeeManager) {
        this.xbeeManager = xbeeManager;
        this.nodes = new ArrayList<>();
    }

    public void discoverNodes() {
        long nodeDiscoveryTimeout = 6000;
        nodes.clear();
        try {
            xbeeManager.sendRemoteAtRequest(new XBeeAddress64(), "ND", new int[]{});
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < nodeDiscoveryTimeout) {
                try {
                    NodeDiscover node = xbeeManager.discoverNode();
                    nodes.add(node);
                    println(node);
                } catch (Exception e) {
                    print(".");
                }
            }
        } catch (Exception e) {
            println("Discovery failed: " + e);
        }
    }

    public ArrayList<NodeDiscover> getNodes() {
        return nodes;
    }
}
