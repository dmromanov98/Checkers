import javafx.application.Platform;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Client implements Runnable {

    private int port;
    private static ClientWindowController swc;

    private void changeStatus(String status) {
        Platform.runLater(() -> swc.changeStatus(status));
    }

    public Client(int port, ClientWindowController swc) {
        this.swc = swc;
        startServer(port);
    }

    public void startServer(int port) {
        this.port = port;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {

    }
}
