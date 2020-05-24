import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public TextField messageTextField = new TextField();
    public HBox lowerHBox = new HBox();
    public Button sendButton = new Button();
    public ListView usersListView;
    public ListView chatListView;

    static DataInputStream in;
    static DataOutputStream out;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            Socket socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            boolean running = true;

            //Getting messages
            Thread thread = new Thread(()->{
                while (running){
                    try {
                        String message = in.readUTF();
                        chatListView.getItems().addAll(makeMessage(message));
                        if (message.equals("/exit")){
                            in.close();
                            out.close();
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        lowerHBox.setHgrow(messageTextField, Priority.ALWAYS);
        lowerHBox.prefHeightProperty().bind(messageTextField.heightProperty());
        chatListView.setFocusTraversable(false);
        usersListView.prefHeightProperty().bind(chatListView.heightProperty());
        chatListView.setCellFactory(param -> new ListCell<String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty||item == null){
                    setGraphic(null);
                    setText(null);
                } else {
                    setMinWidth(100);
                    setMaxWidth(300);
                    setPrefWidth(100);
                    setWrapText(true);
                    setText(item);
                }
            }
        });
    }

    public void sendMessage() throws IOException {
        messageTextField.requestFocus();
        if (messageTextField.getText().trim().equals("")) return;
        chatListView.getItems().addAll(makeMessage(messageTextField.getText()));
        send(messageTextField.getText());
        messageTextField.setText("");
    }

    public static void send(String message) throws IOException {
        if (message.equals("/exit")){
            out.writeUTF("/exit");
            Platform.exit();
        } else {
            out.writeUTF(message);
        }
    }

    public void messageTextFieldKeyListener(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            sendMessage();
        }
    }

    public static String makeMessage(String message){
        return "[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]\n" + message;
    }
}
