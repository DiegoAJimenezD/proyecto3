package co.edu.uqvirtual.markerplace.service;



import co.edu.uqvirtual.markerplace.modelo.Estado;
import co.edu.uqvirtual.markerplace.modelo.Producto;
import co.edu.uqvirtual.markerplace.modelo.Vendedor;

import java.util.ArrayList;

public interface IModelFactoryService {
    public Vendedor crearVendedor(String nombre, String apellido, String cedula, String usuario, String contrasenia);
    public void actualizarVendedor(String nombre, String apellido, String cedula, String usuario, String contrasenia)  ;
    public void eliminarVendedor(String cedula) ;
    public boolean  verificarVendedorExistente(String cedula) ;
    public Vendedor obtenerVendedor(String nombreUsuario) ;
    public ArrayList<Vendedor> obtenerVendedores();



    Producto crearProducto(String nombre, String imagen, String precio, Estado estado, String cedulaVendedor);

    void actualizarProducto(String nombre, String imagen, String precio, Estado estado, String cedulaVendedor);

    void eliminarProducto(String nombre, String cedulaVendedor);

    Producto obtenerProducto(String nombre, String cedulaVendedor);

    boolean verificarProductoExistente(String nombre, String cedulaVendedor);

    ArrayList<Producto> obtenerProductos(String cedulaVendedor);
}
