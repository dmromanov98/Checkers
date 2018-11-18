
public class MenuWindowController {

    public static void setGuIs(GUIs guIs) {
        MenuWindowController.guIs = guIs;
    }

    private static GUIs guIs;

    public void startServerWindow() {
        guIs.initServer();
    }

    public void startClientWindow() {

    }
}
