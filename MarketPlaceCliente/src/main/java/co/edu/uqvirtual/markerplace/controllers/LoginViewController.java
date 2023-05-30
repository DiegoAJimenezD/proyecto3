package co.edu.uqvirtual.markerplace.controllers;

import co.edu.uqvirtual.markerplace.crud.CrudAdminViewController;
import co.edu.uqvirtual.markerplace.crud.CrudVendedorViewController;
import co.edu.uqvirtual.markerplace.exceptions.AccesoNoAutorizadoException;
import co.edu.uqvirtual.markerplace.exceptions.DatosNulosException;
import co.edu.uqvirtual.markerplace.exceptions.VendedorNoExisteException;
import co.edu.uqvirtual.markerplace.modelo.Administrador;
import co.edu.uqvirtual.markerplace.modelo.MarketPlace;
import co.edu.uqvirtual.markerplace.modelo.Vendedor;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginViewController {
    ModelFactoryController modelFactoryController = ModelFactoryController.getInstance();
    CrudAdminViewController crudAdminViewController = new CrudAdminViewController(modelFactoryController);
    CrudVendedorViewController crudVendedorViewController = new CrudVendedorViewController(modelFactoryController);

    public void setModelFactoryController(ModelFactoryController modelFactoryController) {
        this.modelFactoryController = modelFactoryController;
    }

    @FXML
    private TextField txtUsuario_loginView;

    @FXML
    private TextField txtContraseña_loginView;



    @FXML
    private Button btnCancelar;


    private MarketplaceViewController marketplaceViewController;
    @FXML
    void btnCancelar_loginView(ActionEvent event) {
        salir();
    }

    public void salir() {

        Stage stage = (Stage) this.btnCancelar.getScene().getWindow();
        stage.close();
    }

    public void mostrarPestaniaPrincipal(ActionEvent event){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/co/edu/uqvirtual/markerplace/marketplaceView.fxml"));


        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
    @FXML
    void btnIngresar_loginView(ActionEvent actionEvent) throws Exception {
        String usuario=txtUsuario_loginView.getText();
        String contrasenia=txtContraseña_loginView.getText();
        Vendedor vendedor=null;



        if (!(usuario.equals("") && contrasenia.equals(""))){

            crudAdminViewController.verificarLoginAdmin(usuario,contrasenia);

            if(modelFactoryController.isRespuestaAdmin()){



                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/co/edu/uqvirtual/markerplace/marketplaceView.fxml"));


                crudAdminViewController.cambiarEstadoLoginAdmin();

                //System.out.println(modelFactoryController.getMarketPlace().getAdmin().isLogin());




                mostrarPestaniaPrincipal(actionEvent);


            }else{
//                if(crudVendedorViewController.verificarVendedorExistente(usuario)){

               //crudVendedorViewController.verficarLogin(usuario,contrasenia);

                    if(modelFactoryController.isRespuestaVendedor()){
                        Vendedor vendedorLogin = crudVendedorViewController.obtenerVendedor(usuario);

                        crudVendedorViewController.asignarVendedorActualModelFactory(vendedorLogin);
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/co/edu/uqvirtual/markerplace/marketplaceView.fxml"));

                        Parent root = loader.load();

                        modelFactoryController.registrarAccionesSistema("El usuario " +usuario, 1, " Ingreso Login");
                        mostrarPestaniaPrincipal(actionEvent);

                    }else{
                        mostrarMensaje("Datos incorrectos", null, "Asegurese de introducir  los datos de su usuario",
                                Alert.AlertType.ERROR );
                        try {
                            throw new AccesoNoAutorizadoException("El acceso no fue autorizado");
                        }catch (AccesoNoAutorizadoException e){
                            e.printStackTrace();
                            modelFactoryController.registrarAccionesSistema(e.toString(), 3, "Ingreso Login");
                        }

                    }


//                }else{
//                    mostrarMensaje("Datos incorrectos", null, "Asegurese de introducir  los datos de su usuario",
//                            Alert.AlertType.ERROR );
//                    try {
//                        throw new AccesoNoAutorizadoException("El acceso no fue autorizado");
//                    }catch (AccesoNoAutorizadoException e){
//                        e.printStackTrace();
//                        modelFactoryController.registrarAccionesSistema(e.toString(), 3, "Ingreso Login");
//                    }
//
//                }

            }


        }
        else {
            mostrarMensaje("Datos de acceso incompletos", null, "Asegurese de introducir todos los datos",
                    Alert.AlertType.ERROR);
            try {
                throw new AccesoNoAutorizadoException("El acceso no fue autorizado");
            }catch (AccesoNoAutorizadoException e){
                e.printStackTrace();
                modelFactoryController.registrarAccionesSistema(e.toString(), 3, "Ingreso Login");
            }

        }


    }


    private void mostrarMensaje(String titulo, String head, String content, Alert.AlertType tipo) {
        Alert alerta = new Alert(null);
        alerta.setTitle(titulo);
        alerta.setHeaderText(head);
        alerta.setContentText(content);
        alerta.setAlertType(tipo);
        alerta.show();
    }



}
