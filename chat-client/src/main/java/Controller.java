import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    public void sendMessage() {
        if (messageTextField.getText().equals("")) return;
        chatListView.getItems().addAll("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]\n" + messageTextField.getText());
        messageTextField.setText("");
        messageTextField.requestFocus();
    }

    public void messageTextFieldKeyListener(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            sendMessage();
        }
    }
}
