package co.edu.uqvirtual.markerplace.crud;

import co.edu.uqvirtual.markerplace.controllers.ModelFactoryController;
import co.edu.uqvirtual.markerplace.exceptions.DatosNulosException;
import co.edu.uqvirtual.markerplace.exceptions.ProductoNoExiste;
import co.edu.uqvirtual.markerplace.modelo.Estado;
import co.edu.uqvirtual.markerplace.modelo.MarketPlace;
import co.edu.uqvirtual.markerplace.modelo.Producto;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CrudProductoViewController {
    ModelFactoryController modelFactoryController;
    MarketPlace marketPlace;


    public CrudProductoViewController(ModelFactoryController modelFactoryController) {
        this.modelFactoryController = modelFactoryController;
        marketPlace = modelFactoryController.getMarketPlace();
    }
    public ArrayList<Producto> obtenerProductos(String cedula){
        return marketPlace.obtenerProductos(cedula);
    }
    public boolean verificarProductoExistente(String nombre, String cedula){
        return modelFactoryController.verificarProductoExistente(nombre,cedula);
    }
    public Producto crearProducto(String nombre, String imagen, String precio, Estado estado, String cedulaVendedor){
        return modelFactoryController.crearProducto(nombre, imagen, precio, estado, cedulaVendedor);
    }

    public void eliminarProducto(String nombre,String cedulaVendedor) throws ProductoNoExiste {
         modelFactoryController.getMarketPlace().eliminarProducto(nombre, cedulaVendedor);
    }
    public void actualizarProducto(String nombre, String imagen, String precio, Estado estado,String cedulaVendedor) throws DatosNulosException, ProductoNoExiste {
        modelFactoryController.getMarketPlace().actualizarProducto(nombre, imagen, precio, estado, cedulaVendedor);
    }

    public void agregarMegustaProducto(Producto productoSelecionado){
        modelFactoryController.agregarMegustaProducto(productoSelecionado);
    }
    public int retornarCantidadMeGustas(){
        return modelFactoryController.retornarCantidadMeGustas();
    }
    public ArrayList<Producto> obtenerProductosVendedorActual(){
        return modelFactoryController.obtenerProductosVendedorActual();
    }
    public ArrayList<Producto> obtenerProductosAliados(){
        return modelFactoryController.obtenerProductosAliados();
    }
    public ArrayList<Producto> obtenerTopProductos(){
        return modelFactoryController.obtenerTopProductos();
    }

    public String obtenerCantidadProductosPorFecha(LocalDateTime selectedDateTime1, LocalDateTime selectedDateTime2){
        return modelFactoryController.obtenerCantidadProductosPorFecha(selectedDateTime1, selectedDateTime2);
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
}
