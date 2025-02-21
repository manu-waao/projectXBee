import com.rapplogic.xbee.api.XBeeAddress64;

public class XBeeUtils {

    public static String addressToString(XBeeAddress64 address) {
        StringBuilder sb = new StringBuilder();
        for (byte b : address.getAddress()) {
            sb.append(String.format("%02X", b));
            sb.append(":");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    public static void log(String message) {
        System.out.println("[" + System.currentTimeMillis() + "] " + message);
    }

    public static XBeeAddress64 stringToAddress(String hexAddress) {
        String[] parts = hexAddress.split(":");
        byte[] addressBytes = new byte[parts.length];
        for (int i = 0; i < parts.length; i++) {
            addressBytes[i] = (byte) Integer.parseInt(parts[i], 16);
        }
        return new XBeeAddress64(addressBytes);
    }
}
