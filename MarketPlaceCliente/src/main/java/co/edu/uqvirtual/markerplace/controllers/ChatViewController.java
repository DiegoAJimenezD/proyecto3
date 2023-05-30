package co.edu.uqvirtual.markerplace.controllers;

import co.edu.uqvirtual.markerplace.Application;
import co.edu.uqvirtual.markerplace.modelo.Chat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatViewController {

    Application application;
    ModelFactoryController modelFactoryController = ModelFactoryController.getInstance();
    Chat chatSelecionado;

    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;

    @FXML
    private TextArea txtAreaChat_chat;
    @FXML
    private TextArea txtAreaComen_chat;
    @FXML
    private Button btnEnviarComen_chat;

    @FXML
    void initialize() {

    }
    public Application getAplicacion() {
        return application;
    }

    public void salir_ChatViewAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/co/edu/uqvirtual/markerplace/marketplaceView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    public void EnviarComentarioAction(ActionEvent actionEvent) {
        String texto= txtAreaComen_chat.getText();
        modelFactoryController.iniciarChat(texto);
    }
}
