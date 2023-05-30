package co.edu.uqvirtual.markerplace.services;

import co.edu.uqvirtual.markerplace.exceptions.DatosNulosException;
import co.edu.uqvirtual.markerplace.exceptions.ProductoNoExiste;
import co.edu.uqvirtual.markerplace.exceptions.VendedorException;
import co.edu.uqvirtual.markerplace.exceptions.VendedorNoExisteException;
import co.edu.uqvirtual.markerplace.modelo.Estado;
import co.edu.uqvirtual.markerplace.modelo.Producto;
import co.edu.uqvirtual.markerplace.modelo.Vendedor;

import java.util.ArrayList;

public interface IMarketPlaceService {
    public Vendedor crearVendedor(String nombre, String apellido, String cedula, String usuario, String contrasenia) throws DatosNulosException;
    public void actualizarVendedor(String nombre, String apellido, String cedula, String usuario, String contrasenia) throws VendedorException, DatosNulosException;
    public void eliminarVendedor(String cedula)throws VendedorException;
    public boolean  verificarVendedorExistente(String cedula) throws VendedorNoExisteException;;
    public Vendedor obtenerVendedor(String cedula) throws VendedorNoExisteException;
    public ArrayList<Vendedor> obtenerEmpleados();


    public Producto crearProducto(String nombre, String imagen, String precio, Estado estado,String cedulaVendedor, String fechaMomento) throws DatosNulosException;
    public void  actualizarProducto(String nombre, String imagen, String precio, Estado estado,String cedulaVendedor) throws DatosNulosException, ProductoNoExiste;
    public boolean verificarProductoExistente(String nombre,String cedulaVendedor) throws ProductoNoExiste;
    public void eliminarProducto(String nombre,String cedulaVendedor) throws ProductoNoExiste;
    public Producto obtenerProducto(String nombre,String cedulaVendedor) throws ProductoNoExiste;
    public ArrayList<Producto> obtenerProductos(String cedulaVendedor);
}
