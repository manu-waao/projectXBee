package main;

import network.XBeeManager;
import network.NodeDiscoveryManager;
import network.NetworkMonitor;
import network.PerformanceMonitor;
import network.NetworkTester;
import network.NodeManager;
import network.XBeeUtils;

import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;

public class Main {

    public static void main(String[] args) {
        String serialPort = "/dev/tty.usbserial-A1000iMG";
        int baudRate = 9600;

        XBeeManager xbeeManager = new XBeeManager(serialPort, baudRate);
        XBee xbee = xbeeManager.getXBee();

        NodeManager nodeManager = new NodeManager(xbeeManager);
        NetworkTester networkTester = new NetworkTester(xbeeManager);
        PerformanceMonitor performanceMonitor = new PerformanceMonitor(networkTester);
        NetworkMonitor networkMonitor = new NetworkMonitor(nodeManager, networkTester);
        NodeDiscoveryManager nodeDiscoveryManager = new NodeDiscoveryManager(xbeeManager);

        networkMonitor.startMonitoring();
        
        nodeDiscoveryManager.discoverNodes();

        XBeeAddress64 testNodeAddress = XBeeUtils.stringToAddress("0013A20040A1B32A");
        boolean isNodeReachable = networkTester.pingNode(testNodeAddress);
        if (isNodeReachable) {
            System.out.println("Node " + XBeeUtils.addressToString(testNodeAddress) + " is reachable.");
        } else {
            System.out.println("Node " + XBeeUtils.addressToString(testNodeAddress) + " is not reachable.");
        }

        performanceMonitor.measureRTT(testNodeAddress);
        performanceMonitor.printPerformanceStats();
    }
}
