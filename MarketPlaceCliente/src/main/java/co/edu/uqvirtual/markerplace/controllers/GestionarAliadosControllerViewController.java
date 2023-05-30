package co.edu.uqvirtual.markerplace.controllers;

import co.edu.uqvirtual.markerplace.Application;
import co.edu.uqvirtual.markerplace.exceptions.DatosNulosException;
import co.edu.uqvirtual.markerplace.exceptions.VendedorException;
import co.edu.uqvirtual.markerplace.exceptions.VendedorNoExisteException;
import co.edu.uqvirtual.markerplace.modelo.Estado;
import co.edu.uqvirtual.markerplace.modelo.Solicitud;
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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
public class GestionarAliadosControllerViewController {
    @FXML
    public TextField txtGestionarAliados_buscarSugerencias;
    @FXML
    public Label lbIdentificador;

    ModelFactoryController modelFactoryController = ModelFactoryController.getInstance();

    private Application application;
    private ResourceBundle resources;
    private URL location;

    //---------------DECLARACION DE RECURSOS DE VISTA
    //Tablas
    @FXML
    private TableView<Solicitud> tblSolicitudesAmistad_gestionarAliados;
    @FXML
    private TableColumn<Vendedor,String> colNombreSolicitud;
    @FXML
    private TableColumn<Vendedor, String> colApellidoSolicitud;
    //Tabla de sugeridos
    @FXML
    private TableView<Vendedor> tblSugerenciasAmistad_gestionarAliados;
    @FXML
    private TableColumn<Vendedor, String> colNomSugerencia;
    @FXML
    private TableColumn<Vendedor, String> colNomIdentificacion;


    //Botones
    @FXML
    private Button btnRefrescarTbl_gestionarAliados;
    @FXML
    private Button btnBuscar_gestionarAliados;
    @FXML
    private Button btnAceptar_gestionarAliados;
    @FXML
    private Button btnRechazar_GestionarAliados;
    @FXML
    private Button btnEnviarSugeren_gestionarAliados;
    @FXML
    private Button btnSalir_gestionarAliadosView;

    //-------Instancias de los vendedores--------
    Vendedor vendedor;
    Solicitud solicitudSelecionada;
    Vendedor vendedorSelecionado;

    ArrayList<Vendedor> vendedoresMarketPlace = modelFactoryController.getMarketPlace().getListVendedores();

    //-------Obvervables
    ObservableList<Solicitud> listaSolicitudes = FXCollections.observableArrayList();
    ObservableList<Vendedor> listadoVendedoresSugeridos = FXCollections.observableArrayList();

    @FXML
    void initialize() throws IOException {
        vendedor = modelFactoryController.getVendedorActual();

        this.colNombreSolicitud.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colApellidoSolicitud.setCellValueFactory(new PropertyValueFactory<>("cedula"));

        //this.colNomSugerencia.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        //this.colNomIdentificacion.setCellValueFactory(new PropertyValueFactory<>("cedula"));

        tblSolicitudesAmistad_gestionarAliados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            solicitudSelecionada = newSelection;

        });

        tblSugerenciasAmistad_gestionarAliados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)-> {
            vendedorSelecionado = newSelection;
        });


        cargarListaSolicitudes();
        cargarListaSolicitudesSugeridas();
    }



    @FXML
    void salirGestionarAliadossAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/co/edu/uqvirtual/markerplace/marketplaceView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    private void cargarListaSolicitudes() {
        tblSolicitudesAmistad_gestionarAliados.getItems().clear();
        tblSolicitudesAmistad_gestionarAliados.setItems(obtenerSolicitudes());
    }
    private void cargarListaSolicitudesSugeridas() {
        tblSugerenciasAmistad_gestionarAliados.getItems().clear();
        tblSugerenciasAmistad_gestionarAliados.setItems(obtenerSolicitudesSugeridas());
    }

    private ObservableList<Vendedor> obtenerSolicitudesSugeridas() {
        listadoVendedoresSugeridos.addAll(vendedor.getListaAliados());
        return listadoVendedoresSugeridos;
    }

    //Kevin Agrego
    private ObservableList<Solicitud> obtenerSolicitudes() {
        listaSolicitudes.addAll(vendedor.getListaSolicitudes());
        return listaSolicitudes;
    }
    @FXML
    public void btnRechazarAction(ActionEvent actionEvent) throws DatosNulosException, VendedorException, VendedorNoExisteException, IOException {
        eliminarSolicitud();
        cargarListaSolicitudes();
    }
    @FXML
    public void btnAceptarGestionarAliadosAction(ActionEvent actionEvent) {
        aceptarSolicitud();
        cargarListaSolicitudes();
    }

    private void aceptarSolicitud() {
        if(solicitudSelecionada != null){
            if (mostrarMensajeConfirmacion("¿ Esta seguro de aceptar a esta Solicitud ?")) {
                //vendedor = modelFactoryController.getVendedorActual();
                modelFactoryController.aceptarSolicitud(solicitudSelecionada);
                //Accion
                //System.out.println(vendedor.getListaAliados().get(0).getNombre());
            }

        }else {
            modelFactoryController.registrarAccionesSistema("La Solicitud no fue seleccionado", 2, "Seleccionar vendedor");
            modelFactoryController.mostrarMensaje("Notificacion Vendedor", "Vendedor no seleccionado",
                    "Para eliminar un Vendedor debe seleccionar a uno", Alert.AlertType.WARNING);
        }
    }

    private void eliminarSolicitud() throws VendedorNoExisteException, IOException, VendedorException {

        if (solicitudSelecionada != null) {

            if (mostrarMensajeConfirmacion("¿ Esta seguro de eliminar a esta Solicitud ?")) {

//                vendedor = modelFactoryController.getVendedorActual();

                modelFactoryController.eliminarSolicitud(solicitudSelecionada.getCedula());

                modelFactoryController.mostrarMensaje("Notificacion Solicitud", "Solicitud eliminada", "La solicitud Fue eliminada exitosamente",
                        Alert.AlertType.INFORMATION);
                modelFactoryController.registrarAccionesSistema("El vendedor no fue eliminado", 1, "Vendedor eliminado");
                cargarListaSolicitudes();
                tblSolicitudesAmistad_gestionarAliados.refresh();
            } else {
                modelFactoryController.registrarAccionesSistema("La Solicitud no fue eliminada", 3, "Eliminar vendedor");
                modelFactoryController.mostrarMensaje("Notificacion Vendedor", "Vendedor no eliminado", "El Vendedor  no fue eliminado",
                        Alert.AlertType.INFORMATION);
            }

        } else {
            modelFactoryController.registrarAccionesSistema("El vendedor no fue seleccionado", 2, "Seleccionar vendedor");
            modelFactoryController.mostrarMensaje("Notificacion Vendedor", "Vendedor no seleccionado",
                    "Para eliminar un Vendedor debe seleccionar a uno", Alert.AlertType.WARNING);
        }
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

}



