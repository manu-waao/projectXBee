import java.util.ArrayList;

public class PerformanceMonitor {
    private NetworkTester networkTester;
    private ArrayList<Long> pingTimes;

    public PerformanceMonitor(NetworkTester networkTester) {
        this.networkTester = networkTester;
        this.pingTimes = new ArrayList<>();
    }

    public long measureRTT(XBeeAddress64 address) {
        long startTime = System.currentTimeMillis();
        boolean isReachable = networkTester.pingNode(address);
        long endTime = System.currentTimeMillis();

        if (isReachable) {
            long rtt = endTime - startTime;
            pingTimes.add(rtt);
            return rtt;
        } else {
            return -1;
        }
    }

    public double getAveragePingTime() {
        if (pingTimes.isEmpty()) return 0;
        long sum = 0;
        for (long pingTime : pingTimes) {
            sum += pingTime;
        }
        return sum / (double) pingTimes.size();
    }

    public void printPerformanceStats() {
        System.out.println("Average ping time: " + getAveragePingTime() + " ms");
    }
}
