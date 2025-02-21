import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeTimeoutException;
import com.rapplogic.xbee.api.AtCommand;
import com.rapplogic.xbee.api.AtCommandResponse;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zingbee.NodeDiscover;
import com.rapplogic.xbee.api.zingbee.ZNetRemoteAtRequest;
import com.rapplogic.xbee.api.zingbee.ZNetRemoteAtResponse;
import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.XBeeAddress64;

public class XBeeManager {
    private XBee xbee;
    private String port;

    public XBeeManager(String port) {
        this.port = port;
        this.xbee = new XBee();
    }

    public void openConnection() throws XBeeException {
        xbee.open(port, 9600);
    }

    public void closeConnection() {
        if (xbee != null) {
            xbee.close();
        }
    }

    public NodeDiscover discoverNode() throws XBeeTimeoutException {
        return new NodeDiscover();
    }

    public ZNetRemoteAtResponse sendRemoteAtRequest(XBeeAddress64 address, String command, int[] payload) throws Exception {
        ZNetRemoteAtRequest request = new ZNetRemoteAtRequest(address, command, payload);
        return (ZNetRemoteAtResponse) xbee.sendSynchronous(request, 10000);
    }

    public XBeeResponse getResponse(long timeout) throws XBeeTimeoutException {
        return xbee.getResponse(timeout);
    }
}
