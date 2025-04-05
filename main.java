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

import org.w3c.dom.*;
import javax.xml.parsers.*;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            Config config = ConfigLoader.loadConfig("config.xml");
            XBeeManager xbeeManager = new XBeeManager(config.getSerialPort(), config.getBaudRate());
            XBee xbee = xbeeManager.getXBee();

            NodeManager nodeManager = new NodeManager(xbeeManager);
            NetworkTester networkTester = new NetworkTester(xbeeManager);
            PerformanceMonitor performanceMonitor = new PerformanceMonitor(networkTester);
            NetworkMonitor networkMonitor = new NetworkMonitor(nodeManager, networkTester);
            NodeDiscoveryManager nodeDiscoveryManager = new NodeDiscoveryManager(xbeeManager);

            if (config.isNodeDiscoveryEnabled()) {
                nodeDiscoveryManager.discoverNodes();
            }

            networkMonitor.startMonitoring();
            for (String testNodeAddress : config.getTestNodes()) {
                XBeeAddress64 testNodeAddress64 = XBeeUtils.stringToAddress(testNodeAddress);
                boolean isNodeReachable = networkTester.pingNode(testNodeAddress64);
                if (isNodeReachable) {
                    System.out.println("Node " + XBeeUtils.addressToString(testNodeAddress64) + " is reachable.");
                } else {
                    System.out.println("Node " + XBeeUtils.addressToString(testNodeAddress64) + " is not reachable.");
                }

                performanceMonitor.measureRTT(testNodeAddress64);
                performanceMonitor.printPerformanceStats();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




class ConfigLoader {

    public static Config loadConfig(String xmlFilePath) throws Exception {
        File xmlFile = new File(xmlFilePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        String serialPort = doc.getElementsByTagName("serialPort").item(0).getTextContent();
        int baudRate = Integer.parseInt(doc.getElementsByTagName("baudRate").item(0).getTextContent());
        boolean nodeDiscoveryEnabled = Boolean.parseBoolean(doc.getElementsByTagName("enabled").item(0).getTextContent());
        int discoveryTimeout = Integer.parseInt(doc.getElementsByTagName("discoveryTimeout").item(0).getTextContent());

        int pingRetries = Integer.parseInt(doc.getElementsByTagName("pingRetries").item(0).getTextContent());
        int pingTimeout = Integer.parseInt(doc.getElementsByTagName("pingTimeout").item(0).getTextContent());

        boolean performanceMonitorEnabled = Boolean.parseBoolean(doc.getElementsByTagName("enabled").item(0).getTextContent());
        int rttThreshold = Integer.parseInt(doc.getElementsByTagName("rttThreshold").item(0).getTextContent());
        boolean enableLogging = Boolean.parseBoolean(doc.getElementsByTagName("enableLogging").item(0).getTextContent());

        NodeList nodeList = doc.getElementsByTagName("nodeAddress");
        String[] testNodes = new String[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            testNodes[i] = nodeList.item(i).getTextContent();
        }

        return new Config(serialPort, baudRate, nodeDiscoveryEnabled, discoveryTimeout, pingRetries, pingTimeout, 
                performanceMonitorEnabled, rttThreshold, enableLogging, testNodes);
    }
}

class Config {
    private String serialPort;
    private int baudRate;
    private boolean nodeDiscoveryEnabled;
    private int discoveryTimeout;
    private int pingRetries;
    private int pingTimeout;
    private boolean performanceMonitorEnabled;
    private int rttThreshold;
    private boolean enableLogging;
    private String[] testNodes;

    public Config(String serialPort, int baudRate, boolean nodeDiscoveryEnabled, int discoveryTimeout, 
                  int pingRetries, int pingTimeout, boolean performanceMonitorEnabled, 
                  int rttThreshold, boolean enableLogging, String[] testNodes) {
                        this.serialPort = serialPort;
                        this.baudRate = baudRate;
                        this.nodeDiscoveryEnabled = nodeDiscoveryEnabled;
                        this.discoveryTimeout = discoveryTimeout;
                        this.pingRetries = pingRetries;
                        this.pingTimeout = pingTimeout;
                        this.performanceMonitorEnabled = performanceMonitorEnabled;
                        this.rttThreshold = rttThreshold;
                        this.enableLogging = enableLogging;
                        this.testNodes = testNodes;
    }
    public String getSerialPort() { return serialPort; }
    public int getBaudRate() { return baudRate; }
    public boolean isNodeDiscoveryEnabled() { return nodeDiscoveryEnabled; }
    public int getDiscoveryTimeout() { return discoveryTimeout; }
    public int getPingRetries() { return pingRetries; }
    public int getPingTimeout() { return pingTimeout; }
    public boolean isPerformanceMonitorEnabled() { return performanceMonitorEnabled; }
    public int getRttThreshold() { return rttThreshold; }
    public boolean isEnableLogging() { return enableLogging; }
    public String[] getTestNodes() { return testNodes; }
}
