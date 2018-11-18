import javafx.application.Platform;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server implements Runnable {

    private int port;
    private static ServerWindowController swc;

    private void changeStatus(String status) {
        Platform.runLater(() -> swc.changeStatus(status));
    }

    private void changeHost(String host) {
        Platform.runLater(() -> swc.changeHost(host));
    }

    private void changeConnectedUsers(String users) {
        Platform.runLater(() -> swc.changeConnectedUsers(users));
    }

    public Server(int port, ServerWindowController swc) {
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
        try {
            ServerSocket listener = new ServerSocket(port);
            changeStatus("Online");
            changeConnectedUsers("Player 1");
            Socket socket = listener.accept();
            changeConnectedUsers("Player 1, Player 2");

            while (true) {

                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                out.println(new Date().toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
