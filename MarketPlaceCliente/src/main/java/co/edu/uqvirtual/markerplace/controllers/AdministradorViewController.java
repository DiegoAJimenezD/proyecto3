package co.edu.uqvirtual.markerplace.controllers;

import co.edu.uqvirtual.markerplace.exceptions.DatosNoIngresadosException;
import co.edu.uqvirtual.markerplace.exceptions.DatosNulosException;
import co.edu.uqvirtual.markerplace.exceptions.VendedorException;
import co.edu.uqvirtual.markerplace.exceptions.VendedorNoExisteException;
import co.edu.uqvirtual.markerplace.modelo.Usuario;
import co.edu.uqvirtual.markerplace.modelo.Vendedor;

import javafx.application.Application;
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

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdministradorViewController {

    ModelFactoryController modelFactoryController = ModelFactoryController.getInstance();
    public ModelFactoryController getModelFactoryController() {
        return modelFactoryController;
    }

    public void setModelFactoryController(ModelFactoryController modelFactoryController) {
        this.modelFactoryController = modelFactoryController;
        cargarListadoVendedores();
    }

    private Application aplicacion; // SE DECLARA VARIABLE APLICACION CON GETS AND SETS

    private Usuario usuarioSeleccionado;
    private Vendedor vendedorSeleccionado;//SE DECLARIA UNA VARIABLE DE TIPO VENDEDOR PARA ASIGNAR VENDEDOR SELECCIONADO EN OBSERVABLE LIST
    ObservableList<Vendedor> listadoVendedores = FXCollections.observableArrayList();


    //---------------DECLARACION DE RECURSOS DE VISTA
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnEliminar;

    @FXML
    private TableColumn<Vendedor, String> colCedula;

    @FXML
    private TextField txtUsuario;

    @FXML
    private TableColumn<Vendedor, String> colNombre;
    @FXML
    private TableColumn<Vendedor, String> colApellido;

    @FXML
    private TableView<Vendedor> tblListaVendedor;

    @FXML
    private Button btnGuardar;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtContrasenia;

    @FXML
    private TextField txtNombre;

    @FXML
    private Button btnActualizar;

    private void mostraInformacionvendedor(Vendedor vendedor, Usuario usuario) {

        if (vendedor != null) {

            txtNombre.setText(vendedor.getNombre());
            txtApellido.setText(vendedor.getApellido());
            txtCedula.setText(vendedor.getCedula());
            txtUsuario.setText(usuario.getNombreUsuario());
            txtContrasenia.setText(usuario.getContrasenia());

        }
    }
    @FXML
    void initialize() {

        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        this.colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));

        tblListaVendedor.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            vendedorSeleccionado = newSelection;

            mostraInformacionvendedor(vendedorSeleccionado, usuarioSeleccionado);

        });
        cargarListadoVendedores();
    }

    @FXML
    void btnActualizar(ActionEvent event) throws VendedorNoExisteException, DatosNoIngresadosException, IOException, VendedorException {
        actualizarVendedor();
    }
    private void actualizarVendedor() throws  VendedorException {
        String nombre = txtNombre.getText();
        String cedula = txtNombre.getText();
        String apellido = txtApellido.getText();
        String usuario = txtUsuario.getText();
        String contrasenia = txtContrasenia.getText();
        if(vendedorSeleccionado != null){
            if (datosValidos(nombre,apellido ,cedula, usuario, contrasenia)) {

                modelFactoryController.actualizarVendedor(nombre,apellido,cedula,usuario,contrasenia);


                modelFactoryController.mostrarMensaje("Notificacion Vendedor", "Vendedor Actualizado", "El Vendedor ha sido actualizado",
                        Alert.AlertType.INFORMATION);


                cargarListadoVendedores();
                tblListaVendedor.refresh();
                limpiarDatos();
            }
        }

         else {

            modelFactoryController.mostrarMensaje("Notificacion vendedor", "vendedor no seleccionado",
                    "n", Alert.AlertType.WARNING);

        }
    }

    @FXML
    void btnGuardar(ActionEvent event) throws DatosNulosException, IOException {
        guardarVendedor();
    }

    private void guardarVendedor() throws DatosNulosException, IOException  {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String cedula = txtCedula.getText();
        String usuario = txtUsuario.getText();
        String contracenia = txtContrasenia.getText();

        if (vendedorSeleccionado == null) {
            if (datosValidos(nombre, apellido, cedula, usuario, contracenia)) {

                Vendedor vendedor1 = null;
                vendedor1 = modelFactoryController.crearVendedor( nombre, apellido, cedula, usuario, contracenia);
                cargarListadoVendedores();
                if (vendedor1 != null) {
                    limpiarDatos();
                    modelFactoryController.mostrarMensaje("Notificacion Vendedor", "Vendedor registrado",
                            "El Vendedor fue registrado con exito", Alert.AlertType.INFORMATION);
                } else {
                    modelFactoryController.mostrarMensaje("Notificacion Vendedor", "Vendedor no registrado", "El Vendedor no fue registrado",
                            Alert.AlertType.INFORMATION);
                }

            }
        } else {
            modelFactoryController.mostrarMensaje("Notificacion Vendedor", "Vendedor no registrado", "no debe seleccionar un vendedor existente",
                    Alert.AlertType.WARNING);
        }
    }
    private void limpiarDatos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtCedula.setText("");
        txtUsuario.setText("");
        txtContrasenia.setText("");

    }
    @FXML
    void btnEliminar(ActionEvent event) throws VendedorNoExisteException, IOException, VendedorException {
        eliminarVendedor();

    }
    private void eliminarVendedor() throws VendedorNoExisteException, IOException, VendedorException {

        if (vendedorSeleccionado != null) {

            if (mostrarMensajeConfirmacion("Esta seguro de eliminar a este Vendedor?")) {

                modelFactoryController.eliminarVendedor(vendedorSeleccionado.getCedula());

                modelFactoryController.mostrarMensaje("Notificacion Vendedor", "Vendedor eliminado", "El Vendedor fue eliminado con exito",
                        Alert.AlertType.INFORMATION);
                cargarListadoVendedores();
                tblListaVendedor.refresh();
                limpiarDatos();
            } else {
                modelFactoryController.mostrarMensaje("Notificacion Vendedor", "Vendedor no eliminado", "El Vendedor  no fue eliminado",
                        Alert.AlertType.INFORMATION);
            }

        } else {
            modelFactoryController.mostrarMensaje("Notificacion Vendedor", "Vendedor no seleccionado",
                    "Para eliminar un Vendedor debe seleccionar a uno", Alert.AlertType.WARNING);
        }
    }
    private void cargarListadoVendedores() {
        tblListaVendedor.getItems().clear();
        tblListaVendedor.setItems(obtenerVendedores());
    }
    private ObservableList<Vendedor> obtenerVendedores() {
        listadoVendedores.addAll(modelFactoryController.obtenerVendedores());
        return listadoVendedores;
    }
    private boolean datosValidos(String nombre, String apellido, String cedula, String usuario,
                                 String contrasenia) {

        String mensaje = "";

        if (nombre == null || nombre.equals("") || nombre.isEmpty()) {
            mensaje += "El nombre es invalido";
        }
        if (apellido == null || apellido.equals("") || apellido.isEmpty()) {
            mensaje += "El apellido es invalido";
        }
        if (cedula == null || cedula.equals("") || cedula.isEmpty()) {
            mensaje += "El documento es invalido";
        }
        if (usuario == null || usuario.equals("") || usuario.isEmpty()) {
            mensaje += "el usuario es invalido";
        }
        if (contrasenia == null || contrasenia.equals("") || contrasenia.isEmpty()) {
            mensaje += "la contrasenia es invalido";
        }
        if (modelFactoryController.verificarVendedorExistente(cedula) && vendedorSeleccionado == null) {
            mensaje += "Ya existe un vendedor con  documento";
        }

        if (contrasenia.length() > 5) {
            mensaje += "la contrasenia debe tener 5 caracteres o menos";
        }
        if (mensaje.equals("")) {
            return true;
        }
        modelFactoryController.mostrarMensaje("Notificacion Vendedor", "Datos Invalidos", mensaje, Alert.AlertType.WARNING);
        return false;
    }
    private boolean mostrarMensajeConfirmacion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmacion");
        alerta.setContentText(mensaje);
        Optional<ButtonType> action = alerta.showAndWait();
        if (action.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }
    @FXML
    void btnNuevo(ActionEvent event) {

    }

    @FXML
    void salir(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/co/edu/uqvirtual/markerplace/login-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        LoginViewController inicioForm = loader.getController();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
}
