package co.edu.uqvirtual.markerplace.crud;

import co.edu.uqvirtual.markerplace.controllers.ModelFactoryController;
import co.edu.uqvirtual.markerplace.exceptions.VendedorNoExisteException;
import co.edu.uqvirtual.markerplace.modelo.MarketPlace;
import co.edu.uqvirtual.markerplace.modelo.Producto;
import co.edu.uqvirtual.markerplace.modelo.Vendedor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class CrudVendedorViewController {
    ModelFactoryController modelFactoryController;
    MarketPlace marketPlace;


    public void actualizarVendedor(String nombre, String apellido, String cedula, String usuario, String contrasenia){
        modelFactoryController.actualizarVendedor(nombre, apellido, cedula, usuario, contrasenia);
    }
    public Vendedor crearVendedor(String nombre, String apellido, String cedula, String usuario, String contrasenia){
        return modelFactoryController.crearVendedor(nombre, apellido, cedula, usuario, contrasenia);
    }
    public void eliminarVendedor(String cedula){
        modelFactoryController.eliminarVendedor(cedula);
    }
    public ArrayList<Vendedor> obtenerVendedores(){
        return modelFactoryController.obtenerVendedores();
    }
//    public boolean verificarVendedorExistente(String cedula){
//        return modelFactoryController.verificarVendedorExistente(cedula);
//    }
    public CrudVendedorViewController(ModelFactoryController modelFactoryController) {
        this.modelFactoryController = modelFactoryController;
        marketPlace = modelFactoryController.getMarketPlace();
    }
    public boolean verificarVendedorExistente(String usuario) throws VendedorNoExisteException {
        return modelFactoryController.getMarketPlace().verificarVendedorExistente(usuario);
    }
    public void verficarLogin(String usuario, String contrasenia) throws Exception {
          modelFactoryController.iniciarConexionLogin(usuario, contrasenia);

    }
    public Vendedor obtenerVendedor(String usuario) throws VendedorNoExisteException {
        return modelFactoryController.getMarketPlace().obtenerVendedor(usuario);

    }
    public void asignarVendedorActualModelFactory(Vendedor vendedor){
        modelFactoryController.setVendedorActual(vendedor);
    }
    public Vendedor traerVendedorActalModelFactory(){
        return modelFactoryController.getVendedorActual();
    }

    public ArrayList<Vendedor> obtenerVendedorAliado(){
        return modelFactoryController.obtenerVendedorAliado();
    }
    public void quitarLoginVendedor(){
        modelFactoryController.quitarLoginVendedor();
    }
    public int calcularChatsVendedores(String nombre1, String nombre2){
        return modelFactoryController.calcularChatsVendedores(nombre1, nombre2);
    }

    public void salvarDatos() {


        modelFactoryController.salvarDatos();
    }




    public MarketPlace getMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(MarketPlace marketPlace) {
        this.marketPlace = marketPlace;
    }
    public Optional<Vendedor> buscarVendedorLog(){
        return modelFactoryController.buscarVendedorLog();
    }
    public Vendedor obtenerVendedorActual(){
        return modelFactoryController.getVendedorActual();
    }
    public int obtenerPosicionVendedorLog(){
        return  modelFactoryController.obtenerPosicionLog();
    }

}
