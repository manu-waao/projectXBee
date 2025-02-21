import com.rapplogic.xbee.api.XBeeAddress64;
import processing.core.PImage;

public class Switch {
    private int switchNumber, posX, posY;
    private boolean state = false;
    private XBeeAddress64 addr64;
    private String address;
    private PImage on, off;

    public Switch(XBeeAddress64 addr64, int switchNumber) {
        on = loadImage("on.jpg");
        off = loadImage("off.jpg");
        this.addr64 = addr64;
        this.switchNumber = switchNumber;
        this.posX = switchNumber * (on.width + 40) + 40;
        this.posY = 50;

        String[] hexAddress = new String[addr64.getAddress().length];
        for (int i = 0; i < addr64.getAddress().length; i++) {
            hexAddress[i] = String.format("%02x", addr64.getAddress()[i]);
        }
        address = join(hexAddress, ":");
        println(address);
    }

    public void render() {
        noStroke();
        if (state) image(on, posX, posY);
        else image(off, posX, posY);

        textAlign(CENTER);
        fill(0);
        textSize(10);
        text(address, posX + on.width / 2, posY + on.height + 10);

        String stateText = "OFF";
        fill(255, 0, 0);
        if (state) {
            stateText = "ON";
            fill(0, 127, 0);
        }
        text(stateText, posX + on.width / 2, posY - 8);
    }

    public void toggleState(XBeeManager xbeeManager) {
        if (mouseX >= posX && mouseY >= posY && mouseX <= posX + on.width && mouseY <= posY + on.height) {
            println(address);
            state = !state;
            try {
                int[] command = {state ? 5 : 4};
                ZNetRemoteAtResponse response = xbeeManager.sendRemoteAtRequest(addr64, "DO", command);
                if (!response.isOk()) {
                    throw new RuntimeException(response.getStatus());
                }
            } catch (Exception e) {
                println(e.getMessage());
                state = !state;
            }
        }
    }

    public void getState(XBeeManager xbeeManager) {
        try {
            ZNetRemoteAtRequest request = new ZNetRemoteAtRequest(addr64, "DO");
            ZNetRemoteAtResponse response = (ZNetRemoteAtResponse) xbeeManager.sendRemoteAtRequest(addr64, "DO", new int[]{});
            if (response.isOk()) {
                int[] responseArray = response.getValue();
                int responseInt = responseArray[0];
                if (responseInt == 4 || responseInt == 5) {
                    state = (responseInt == 5);
                }
            } else {
                throw new RuntimeException(response.getStatus());
            }
        } catch (Exception e) {
            println(e.getMessage());
        }
    }
}
