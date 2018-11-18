import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ServerWindowController {
    @FXML
    StackPane pane;

    @FXML
    JFXTextField fieldPort;

    @FXML
    Label labelHost;

    @FXML
    Label labelStatus;

    @FXML
    Label labelConnectedUsers;

    @FXML
    JFXButton btnStartServer;

    Server server;

    public void dialogWindow(String cause, String message, StackPane stackPane) {

        JFXDialogLayout content = new JFXDialogLayout();
        Text txt = new Text(cause);
        txt.setFont(Font.font("Verdana", 18));
        content.setHeading(txt);
        content.setBody(new Text(message));
        JFXButton btn = new JFXButton("Ok");
        final JFXDialog dialog;

        dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        content.setActions(btn);
        dialog.show();
    }

    private static boolean testOnNumber(String string) {
        String numPattern = "[0-9]+";
        return string.matches(numPattern);
    }

    public void startGame(ActionEvent actionEvent) {
    }

    public void changeStatus(String status) {
        labelStatus.setText("Status: " + status);
    }

    public void changeHost(String host) {
        labelHost.setText("Host: " + host);
    }

    public void changeConnectedUsers(String connected){
        labelConnectedUsers.setText("Connected users: "+connected);
    }

    public void startServer(ActionEvent actionEvent) {
        if (testOnNumber(fieldPort.getText())) {
            int port = Integer.valueOf(fieldPort.getText());
            server = new Server(port, this);
        } else {
            dialogWindow("INFO", "You must enter a number in the port field", pane);
        }
    }
}
