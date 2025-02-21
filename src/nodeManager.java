import com.rapplogic.xbee.api.zingbee.NodeDiscover;
import com.rapplogic.xbee.api.XBeeAddress64;
import java.util.ArrayList;

public class NodeManager {
    private ArrayList<NodeDiscover> nodes;
    private XBeeManager xbeeManager;

    public NodeManager(XBeeManager xbeeManager) {
        this.xbeeManager = xbeeManager;
        this.nodes = new ArrayList<>();
    }

    public void addNode(NodeDiscover node) {
        nodes.add(node);
    }

    public void removeNode(XBeeAddress64 address) {
        nodes.removeIf(node -> node.getNodeAddress64().equals(address));
    }

    public ArrayList<NodeDiscover> getNodes() {
        return nodes;
    }

    public boolean resetNode(XBeeAddress64 address) {
        try {
            ZNetRemoteAtRequest request = new ZNetRemoteAtRequest(address, "ND");
            ZNetRemoteAtResponse response = (ZNetRemoteAtResponse) xbeeManager.sendRemoteAtRequest(address, "ND", new int[]{});
            
            if (response.isOk()) {
                System.out.println("Node reset successfully: " + address);
                return true;
            } else {
                System.out.println("Failed to reset node: " + address);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Reset failed: " + e.getMessage());
            return false;
        }
    }
}
