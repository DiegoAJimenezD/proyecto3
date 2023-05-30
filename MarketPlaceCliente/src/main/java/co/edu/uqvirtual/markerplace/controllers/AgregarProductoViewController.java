package co.edu.uqvirtual.markerplace.controllers;

import co.edu.uqvirtual.markerplace.crud.CrudProductoViewController;
import co.edu.uqvirtual.markerplace.crud.CrudVendedorViewController;
import co.edu.uqvirtual.markerplace.exceptions.DatosNulosException;
import co.edu.uqvirtual.markerplace.exceptions.ErrorCargarDatos;
import co.edu.uqvirtual.markerplace.exceptions.ProductoNoExiste;
import co.edu.uqvirtual.markerplace.exceptions.VendedorNoExisteException;
import co.edu.uqvirtual.markerplace.modelo.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.util.ResourceBundle;

public class AgregarProductoViewController {

    Vendedor vendedor;
    ObservableList<String> estados = FXCollections.observableArrayList("Vendido", "Publicado", "Cancelado");
    @FXML
    private TextField txtAgregarProducto_nombre;
    @FXML
    private TextField txtAgregarProducto_precio;
    @FXML
    private ComboBox<String> cbAgregarProducto_estado;
    @FXML
    private TextField txtAgregarProducto_rutaImagen;
    @FXML
    private Button btnAgreImagen_AgregarProducto;
    @FXML
    private Button btnActualizarTbl_agregarProducto;
    @FXML
    private Button btnGuardarTbl_AgregarProducto;
    @FXML
    private Button btnEliminarTbl_AgregarProducto;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, String> colEstado;
    @FXML
    private TableColumn<Producto, String> colPrecio;
    @FXML
    private TableView<Producto> tblAgregarProducto_listaProducto;
    private Producto productoSeleccionado;
    ObservableList<Producto> listadoProductos = FXCollections.observableArrayList();

    ModelFactoryController modelFactoryController = ModelFactoryController.getInstance();

    CrudProductoViewController crudProductoViewController;
    CrudVendedorViewController crudVendedorViewController;


    Vendedor vende;



    @FXML
    void initialize() throws IOException, VendedorNoExisteException {

        crudProductoViewController = new CrudProductoViewController(modelFactoryController);
        crudVendedorViewController = new CrudVendedorViewController(modelFactoryController);

        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        this.colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tblAgregarProducto_listaProducto.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            //mostraInformacionvendedor(vendedorSeleccionado, usuarioSeleccionado);
            productoSeleccionado = newSelection;
            mostraInformacionProdcuto(productoSeleccionado);

        });
        //cargarListadoProductos();

    }


    @FXML
    void salir_agregarProductoAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/co/edu/uqvirtual/markerplace/marketplaceView.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    void guardarProducto_agregarProductoAction(ActionEvent event) throws DatosNulosException, IOException {
        guardarProducto();
        cargarListadoProductos();
    }
    public void eliminarProducto_agregarProductoAction(ActionEvent actionEvent) throws DatosNulosException {
        eliminarProducto();
        cargarListadoProductos();
    }
    public void actualizarProducto_agregarProductoAction(ActionEvent actionEvent) throws DatosNulosException {
        actualizarProducto();
        cargarListadoProductos();
    }

    MarketplaceViewController market = new MarketplaceViewController();

    private void cargarListadoProductos() {
        tblAgregarProducto_listaProducto.getItems().clear();
        tblAgregarProducto_listaProducto.setItems(obtenerProductos());
    }
    private void mostraInformacionProdcuto(Producto producto) {

        if (producto != null) {

            txtAgregarProducto_nombre.setText(producto.getNombre());
            txtAgregarProducto_precio.setText(producto.getPrecio());
            txtAgregarProducto_rutaImagen.setText(producto.getImagen());
            cbAgregarProducto_estado.setValue(transformarValorCombox(producto.getEstado()));


        }else{
            try {
                throw new DatosNulosException("Producto no existe");

            }catch (Exception e){
                modelFactoryController.registrarAccionesSistema(e.toString(), 2, "Datos nulos");
            }
        }
    }
    public String transformarValorCombox(Estado estado){
        switch (estado){
            case PUBLICADO:
                return "Publicado";

            case CANCELADO:
                return "Cancelado";

            case VENDIDO:
                return "Vendido";

            default:
                try {
                    throw new ErrorCargarDatos("Datos incorrectos");

                }catch (Exception e){
                    modelFactoryController.registrarAccionesSistema(e.toString(), 2, "Datos nulos");
                }

        }
        return "";
    }

    private ObservableList<Producto> obtenerProductos() {
        listadoProductos.addAll(crudProductoViewController.obtenerProductos(vende.getCedula()));
        //listadoProductos.addAll(modelFactoryController.obtenerProductos("1234"));
        return listadoProductos;
    }


    private boolean datosValidos(String nombre, String imagen, String precio, Estado estado) throws DatosNulosException {

        String mensaje = "";

        if (nombre == null || nombre.equals("") || nombre.isEmpty()) {
            mensaje += "El nombre es invalido";
        }
        if (imagen == null || imagen.equals("") || imagen.isEmpty()) {
            mensaje += "La categoría es invalida";
        }
        if (precio == null || precio.equals("") || precio.isEmpty()) {
            mensaje += "El precio es invalido";
        }
        if (estado == null || estado.equals("")) {
            mensaje += "La ruta es invalida";
        }
        if (modelFactoryController.verificarProductoExistente(nombre, vende.getCedula()) && productoSeleccionado == null) {
            mensaje += "Ya existe un producto con ese nombre";



            modelFactoryController.registrarAccionesSistema("Producto ya existe ", 2, "Datos nulos");


        }
        if (mensaje.equals("")) {
            return true;
        }
        modelFactoryController.mostrarMensaje("Notificacion Producto", "Datos Invalidos", mensaje, Alert.AlertType.WARNING);

        return false;
    }


    public void listaEstado(Event event) {
        llenarCombo(cbAgregarProducto_estado, estados);

    }
    public static void llenarCombo(ComboBox<String> comboBox, ObservableList<String> infoCombo){
        comboBox.setItems(infoCombo);
    }
    public Estado colocarValorEstadoProducto(String tipoEstado){
        switch (tipoEstado){
            case "Publicado":
                return Estado.PUBLICADO;


            case"Cancelado":
                return Estado.CANCELADO;

            case"Vendido":
                return Estado.VENDIDO;

            default:
                try {
                    throw new ErrorCargarDatos("Datos incorrectos");

                }catch (Exception e){
                    modelFactoryController.registrarAccionesSistema(e.toString(), 2, "Datos nulos");
                }

        }

        return null;
    }
    private void guardarProducto() throws DatosNulosException, IOException  {
        String nombre = txtAgregarProducto_nombre.getText();
        String imagen = txtAgregarProducto_rutaImagen.getText();
        String precio = txtAgregarProducto_precio.getText();
        Estado estado = colocarValorEstadoProducto(cbAgregarProducto_estado.getValue());

        if (productoSeleccionado == null) {
            if (datosValidos(nombre, imagen, precio, estado)) {

                Producto producto = null;
                //producto = modelFactoryController.crearProducto( nombre, imagen, precio, estado, vendedor);
                producto = crudProductoViewController.crearProducto( nombre, imagen, precio, estado,vende.getCedula());
                crudProductoViewController.salvarDatos();
                cargarListadoProductos();
                if (producto != null) {
                    limpiarCampos();
                    modelFactoryController.mostrarMensaje("Notificacion Producto", "Producto registrado",
                            "El producto fue registrado con exito", Alert.AlertType.INFORMATION);
                } else {
                    modelFactoryController.mostrarMensaje("Notificacion Producto", "Producto registrado exitosamente", "El producto fue registrado exitosamente",
                            Alert.AlertType.INFORMATION);
                }
            }else{
                try {
                    throw new DatosNulosException("Datos nulos al agregar producto");

                }catch (Exception e){
                    modelFactoryController.registrarAccionesSistema(e.toString(), 2, "Datos nulos");
                }
            }
        } else {

            modelFactoryController.mostrarMensaje("Notificacion Producto", "Producto no registrado", "no debe seleccionar un vendedor existente",
                    Alert.AlertType.WARNING);
            try {
                throw new DatosNulosException("Datos nulos al agregar producto");

            }catch (Exception e){
                modelFactoryController.registrarAccionesSistema(e.toString(), 2, "Datos nulos");
            }

        }
    }


    public void eliminarProducto() throws DatosNulosException {
        String nombre = txtAgregarProducto_nombre.getText();
        String imagen = txtAgregarProducto_rutaImagen.getText();
        String precio = txtAgregarProducto_precio.getText();
        Estado estado = colocarValorEstadoProducto(cbAgregarProducto_estado.getValue());

        if (productoSeleccionado != null) {
            if (datosValidos(nombre, imagen, precio, estado)) {


                //producto = modelFactoryController.crearProducto( nombre, imagen, precio, estado, vendedor);
                try {
                    crudProductoViewController.eliminarProducto( nombre, vende.getCedula());
                    crudProductoViewController.salvarDatos();
                } catch (ProductoNoExiste e) {
                    //throw new RuntimeException(e);
                    modelFactoryController.mostrarMensaje("Error al eliminar", "Prodcuto no borrado", e.getMessage(), Alert.AlertType.ERROR);
                }
                cargarListadoProductos();
                modelFactoryController.mostrarMensaje("Notificacion Producto", "Producto borrado exitosamente", "Producto borrado", Alert.AlertType.CONFIRMATION);

                limpiarCampos();
            }else{
                try {
                    throw new DatosNulosException("Datos nulos al agregar producto");

                }catch (Exception e){
                    modelFactoryController.registrarAccionesSistema(e.toString(), 2, "Datos nulos");
                }
            }
        } else {
            modelFactoryController.mostrarMensaje("Notificacion Producto", "Producto no registrado", "No se selecciono algun producto o valores erroneos",
                    Alert.AlertType.WARNING);
        }

    }
    public void limpiarCampos(){
        txtAgregarProducto_nombre.setText("");
        txtAgregarProducto_rutaImagen.setText("");
        txtAgregarProducto_precio.setText("");
        cbAgregarProducto_estado.setValue("");
    }
    public void actualizarProducto() throws DatosNulosException {
        String nombre = txtAgregarProducto_nombre.getText();
        String imagen = txtAgregarProducto_rutaImagen.getText();
        String precio = txtAgregarProducto_precio.getText();
        Estado estado = colocarValorEstadoProducto(cbAgregarProducto_estado.getValue());
        if (productoSeleccionado != null) {
            if (datosValidos(nombre, imagen, precio, estado)) {


                //producto = modelFactoryController.crearProducto( nombre, imagen, precio, estado, vendedor);
                try {
                    crudProductoViewController.actualizarProducto( nombre,imagen, precio, estado,vende.getCedula());
                    crudProductoViewController.salvarDatos();
                } catch (ProductoNoExiste e) {
                    //throw new RuntimeException(e);
                    modelFactoryController.mostrarMensaje("Notificacion Producto", "Producto no actualizado ", "Producto no actualizado"+e.getMessage(), Alert.AlertType.ERROR);
                }
                cargarListadoProductos();
                modelFactoryController.mostrarMensaje("Notificacion Producto", "Producto actualizado exitosamente", "Producto actualizado", Alert.AlertType.CONFIRMATION);

                limpiarCampos();
            }else{
                try {
                    throw new DatosNulosException("Datos nulos al agregar producto");

                }catch (Exception e){
                    modelFactoryController.registrarAccionesSistema(e.toString(), 2, "Datos nulos");
                }
            }
        } else {
            modelFactoryController.mostrarMensaje("Notificacion Producto", "Producto no registrado", "No se selecciono algun producto o valores erroneos",
                    Alert.AlertType.WARNING);
        }

    }


    public void recibirNombre(String lbNombre1) throws VendedorNoExisteException {
        vende = crudVendedorViewController.traerVendedorActalModelFactory();
        cargarListadoProductos();
    }




    /*
    * return switch (tipoEstado) {
            case "Publicado" -> Estado.PUBLICADO;
            case "Cancelado" -> Estado.CANCELADO;
            case "Vendido" -> Estado.VENDIDO;
            default -> null;
        };*/
}
