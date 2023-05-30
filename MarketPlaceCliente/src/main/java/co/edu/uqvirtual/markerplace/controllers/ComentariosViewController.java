package co.edu.uqvirtual.markerplace.controllers;

import co.edu.uqvirtual.markerplace.exceptions.DatosNulosException;
import co.edu.uqvirtual.markerplace.exceptions.VendedorNoExisteException;
import co.edu.uqvirtual.markerplace.modelo.Comentario;
import co.edu.uqvirtual.markerplace.modelo.MarketPlace;
import co.edu.uqvirtual.markerplace.modelo.Producto;
import co.edu.uqvirtual.markerplace.modelo.Vendedor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jdk.jshell.spi.SPIResolutionException;
import javax.swing.text.TabableView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ComentariosViewController {

    ModelFactoryController modelFactoryController = ModelFactoryController.getInstance();
    Comentario comentario;
    Vendedor vendedorEnviado;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button butComenmtar;
    @FXML
    private Button btnSalir;

    @FXML
    private TableColumn<Comentario, String> colNombre;
    @FXML
    private TableColumn<Comentario, String> colCedula;
    @FXML
    private TableColumn<Comentario, String> colComentario;
    @FXML
    private TableView<Comentario> tblComentarios;

    @FXML
    private TextField texComentarios;
    Producto productoSelecionado = modelFactoryController.getProductoActualComentario();
    ObservableList<Comentario> listadoComentarios = FXCollections.observableArrayList();



    @FXML
    void initialize() throws IOException {

        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colCedula.setCellValueFactory(new PropertyValueFactory<>("identificacion"));
        this.colComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));

        tblComentarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {


        });
        cargarListadoProductos();

    }



    @FXML
    public void tbSalirAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/co/edu/uqvirtual/markerplace/marketplaceView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
    @FXML
    public  void butComenmtarAction(ActionEvent actionEvent) throws IOException{
        agregarComentario();
        cargarComentarios();
    }


    private void agregarComentario() {

        vendedorEnviado = modelFactoryController.getVendedorActual();
        productoSelecionado = modelFactoryController.getProductoActualComentario();
        String nombre = modelFactoryController.getVendedorActual().getNombre();
        String identificacion = modelFactoryController.getVendedorActual().getCedula();
        String comentario = texComentarios.getText();

        if (productoSelecionado != null){
            if (texComentarios != null){
                Comentario comentario1 = null;
                comentario1 = modelFactoryController.crearComentario(vendedorEnviado, productoSelecionado, nombre, identificacion, comentario);
                cargarComentarios();
                productoSelecionado.getListaComentarioProductos().add(comentario1);

                if (comentario1 != null) {
                    limpiarCampos();
                    modelFactoryController.mostrarMensaje("Notificacion Comentario ", "Comentario registrado",
                            "El comentario fue registrado con exito", Alert.AlertType.INFORMATION);
                } else {
                    modelFactoryController.mostrarMensaje("Notificacion Comentario", "Comentario registrado exitosamente", "El Comentario fue registrado exitosamente",
                            Alert.AlertType.INFORMATION);
                }
            }else{
                try {
                    throw new DatosNulosException("Datos nulos al agregar un comentario");

                }catch (Exception e){
                    modelFactoryController.registrarAccionesSistema(e.toString(), 2, "Datos nulos");
                }
            }
        } else {

            modelFactoryController.mostrarMensaje("Notificacion Producto", "Producto no registrado", "no debe seleccionar un vendedor existente",
                    Alert.AlertType.WARNING);
            try {
                throw new DatosNulosException("Datos nulos al agregar un comentario");

            }catch (Exception e){
                modelFactoryController.registrarAccionesSistema(e.toString(), 2, "Datos nulos");
            }
        }
    }



    private void cargarComentarios() {
    }
    private void cargarListadoProductos() {
        tblComentarios.getItems().clear();
        tblComentarios.setItems(obtenerComentarios());
    }

    private ObservableList <Comentario> obtenerComentarios() {
        listadoComentarios.addAll(productoSelecionado.getListaComentarioProductos());
        return listadoComentarios;
    }

    public void limpiarCampos(){
        texComentarios.setText("");
    }
}
