import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.zingbee.ZNetRemoteAtRequest;
import com.rapplogic.xbee.api.zingbee.ZNetRemoteAtResponse;
import com.rapplogic.xbee.api.XBeeTimeoutException;

public class NetworkTester {
    private XBeeManager xbeeManager;

    public NetworkTester(XBeeManager xbeeManager) {
        this.xbeeManager = xbeeManager;
    }

    public boolean pingNode(XBeeAddress64 address) {
        try {
            ZNetRemoteAtRequest request = new ZNetRemoteAtRequest(address, "ND");
            ZNetRemoteAtResponse response = (ZNetRemoteAtResponse) xbeeManager.sendRemoteAtRequest(address, "ND", new int[]{});
            
            if (response.isOk()) {
                System.out.println("Node is responsive: " + address);
                return true;
            } else {
                System.out.println("Node unreachable: " + address);
                return false;
            }
        } catch (XBeeTimeoutException e) {
            System.out.println("Ping failed due to timeout: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Ping failed: " + e.getMessage());
            return false;
        }
    }
}
