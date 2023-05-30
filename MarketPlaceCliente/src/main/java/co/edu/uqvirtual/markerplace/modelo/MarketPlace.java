package co.edu.uqvirtual.markerplace.modelo;

import co.edu.uqvirtual.markerplace.exceptions.DatosNulosException;
import co.edu.uqvirtual.markerplace.exceptions.ProductoNoExiste;
import co.edu.uqvirtual.markerplace.exceptions.VendedorException;
import co.edu.uqvirtual.markerplace.exceptions.VendedorNoExisteException;
import co.edu.uqvirtual.markerplace.services.IMarketPlaceService;
//import io.reactivex.Observable;


import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class MarketPlace implements IMarketPlaceService, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private  ArrayList<Vendedor> listaVendedores = new ArrayList<>();



    //Kevin
    private Administrador admin;




    public Administrador getAdmin() {
        return admin;
    }

    public void setAdmin(Administrador admin) {
        this.admin = admin;
    }

    public ArrayList<Vendedor> getListaVendedores() {
        return listaVendedores;
    }

    public void setListaVendedores(ArrayList<Vendedor> listaVendedores) {
        this.listaVendedores = listaVendedores;
    }








    public MarketPlace() {
        admin = new Administrador("Erik", "Triviño", "1234", new Usuario("admin", "admin"));

    }
    public boolean obtenerAdmin(String nombreUsuario, String contrsenia){
        if(nombreUsuario.equals(admin.getUsuario().getNombreUsuario()) && contrsenia.equals(admin.getUsuario().getContrasenia())){
            return true;
        }else{
            return false;
        }
    }
    public Vendedor obtenerVendedorLogin(String nombreVendedor){
        return listaVendedores.stream().filter(x->x.getNombre().equals(nombreVendedor)).findFirst().get();
    }

    public ArrayList<Vendedor> getListVendedores() {
        return listaVendedores;
    }

    @Override
    public Vendedor crearVendedor(String nombre, String apellido, String cedula, String usuario, String contrasenia) throws  DatosNulosException {

        if (nombre == null && apellido == null && usuario == null && contrasenia == null) {
            throw new DatosNulosException("Los datos estan nulos");
        }
        Vendedor v1 = new Vendedor(nombre, apellido, cedula, new Usuario(usuario, contrasenia));

        listaVendedores.add(v1);

        return v1;
    }
@Override
    public void actualizarVendedor(String nombre, String apellido, String cedula, String usuario, String contrasenia) throws VendedorException {


    int pos = obtenerPosicionVendedor(cedula);

    Vendedor v1 =  listaVendedores.get(pos);
    v1.setNombre(nombre);
    v1.setApellido(apellido);
    v1.setCedula(cedula);
    v1.getUsuario().setNombreUsuario(usuario);
    v1.getUsuario().setContrasenia(contrasenia);



    listaVendedores.set(pos, v1);

}
    public int obtenerPosicionVendedor(String cedula){
        int posicion = 0;
        boolean flagEncontrado = false;
        do {
            for(int i = 0; i < listaVendedores.size(); i++){
                if (listaVendedores.get(i).getCedula().equals(cedula)) {
                    posicion = i;
                    flagEncontrado = true;
                }
            }
            if(flagEncontrado == false){
                posicion = -1;

            }

        }while(flagEncontrado == false);
        return posicion;
    }

    @Override
    public void eliminarVendedor(String cedula) throws VendedorException {
        int pos = obtenerPosicionVendedor(cedula);
        listaVendedores.remove(pos);

    }

    @Override
    public boolean verificarVendedorExistente(String usuario) throws VendedorNoExisteException {

       Optional<Vendedor> v1 =  listaVendedores.stream().filter(x-> x.getUsuario().getNombreUsuario().equals(usuario)).findFirst();
        if(v1.isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public int obtenerPosicionSolicitud(String cedula, Vendedor vendedorActual){
        int posicion = 0;
        boolean flagEncontrado = false;
        int posVendedor = obtenerPosicionVendedor(cedula);
        do {
            for(int i = 0; i < vendedorActual.getListaSolicitudes().size() ; i++){
                if ( vendedorActual.getListaSolicitudes().get(i).getCedula().equals(cedula)) {
                    posicion = i;
                    flagEncontrado = true;
                }
            }
            if(flagEncontrado == false){
                posicion = -1;

            }

        }while(flagEncontrado == false);
        return posicion;
    }

    public void eliminarSolicitud(String cedula, Vendedor vendedorActual) throws VendedorException {

        int pos = obtenerPosicionSolicitud(cedula, vendedorActual);
        vendedorActual.getListaSolicitudes().remove(pos);
    }
    public void aceptarSolicitud(Solicitud solicitud, Vendedor vendedorActual) {
        int posicionVendedor = obtenerPosicionVendedor(solicitud.getCedula());
        int posicionSolicitud = obtenerPosicionSolicitud(solicitud.getCedula(), vendedorActual);


        vendedorActual.getListaAliados().add(listaVendedores.get(posicionVendedor));
        vendedorActual.getListaAliados().add(solicitud.getVendedorEnviado());
        vendedorActual.getListaSolicitudes().remove(posicionSolicitud);

        solicitud.getVendedorEnviado().getListaAliados().add(vendedorActual);



    }

    @Override
    public Vendedor obtenerVendedor(String nombreUsuario) throws VendedorNoExisteException {
       // return new Vendedor("Diego", "Jimenez", "1234", new Usuario("1234", "1234"));

        return listaVendedores.stream().filter(x -> x.getUsuario().getNombreUsuario().equals(nombreUsuario)).findFirst().get();
    }
    public Vendedor obtenerVendedorPorNombre(String nombreVendedor) throws VendedorNoExisteException {
        // return new Vendedor("Diego", "Jimenez", "1234", new Usuario("1234", "1234"));

        return listaVendedores.stream().filter(x -> x.getNombre().equals(nombreVendedor)).findFirst().get();
    }

    @Override
    public ArrayList<Vendedor> obtenerEmpleados() {
        return listaVendedores;
    }
    //Kevin Agrego


    @Override
    public Producto crearProducto(String nombre, String imagen, String precio, Estado estado,String cedulaVendedor, String fechaMomento) throws DatosNulosException {
       Producto p1 = new Producto(nombre, imagen, precio, estado);
       p1.setFechaPublicaion(fechaMomento);
       int pos = obtenerPosicionVendedor(cedulaVendedor);
       listaVendedores.get(pos).getListaProductos().add(p1);


        return  p1;
    }

    @Override
    public void actualizarProducto(String nombre, String imagen, String precio, Estado estado,String cedulaVendedor) throws DatosNulosException, ProductoNoExiste {
        int posVendedor = obtenerPosicionVendedor(cedulaVendedor);
        Optional<Producto> p1 = listaVendedores.get(posVendedor).getListaProductos().stream().filter(x -> x.getNombre().equals(nombre)).findFirst();

        if(p1.isPresent()){

            int posPro = obtenerPosicionProducto(nombre, listaVendedores.get(posVendedor).getListaProductos());
            listaVendedores.get(posVendedor).getListaProductos().set(posPro, new Producto(nombre,imagen,  precio, estado));


        }else{
            throw new ProductoNoExiste("El producto no existe");
        }



    }
    public int obtenerPosicionProducto(String nombre, ArrayList<Producto> listaProductos){
        int posicion = 0;
        boolean flagEncontrado = false;
        do {
            for(int i = 0; i < listaProductos.size(); i++){
                if (listaProductos.get(i).getNombre().equals(nombre)) {
                    posicion = i;
                    flagEncontrado = true;
                }
            }
            if(flagEncontrado == false){
                posicion = -1;

            }

        }while(flagEncontrado == false);
        return posicion;
    }


    @Override
    public boolean verificarProductoExistente(String nombre,String cedulaVendedor) throws ProductoNoExiste {
        int posVendedor = obtenerPosicionVendedor(cedulaVendedor);
        Optional<Producto> p1 = listaVendedores.get(posVendedor).getListaProductos().stream().filter(x -> x.getNombre().equals(nombre)).findFirst();

        if(p1.isPresent()){
            return true;
        }else{
            return false;
        }
    }
    /*
    public boolean verificarSolicitudExistente(String nombre,String cedulaVendedor) throws ProductoNoExiste {
        int posVendedor = obtenerPosicionVendedor(cedulaVendedor);
        Optional<Solicitud> s1 = listaVendedores.get(posVendedor).getListaSolicitudes().stream().filter(x -> x.getNombre().equals(nombre)).findFirst();
        if(s1.isPresent()){
            return true;
        }else{
            return false;
        }
    }
     */


    @Override
    public void eliminarProducto(String nombre,String cedulaVendedor) throws ProductoNoExiste {

        int posVendedor = obtenerPosicionVendedor(cedulaVendedor);
        int posProducto  = obtenerPosicionProducto(nombre, listaVendedores.get(posVendedor).getListaProductos());

        Optional<Producto> p1 = listaVendedores.get(posVendedor).getListaProductos().stream().filter(x -> x.getNombre().equals(nombre)).findFirst();

        if(p1.isPresent()){
            listaVendedores.get(posVendedor).getListaProductos().remove(posProducto);

        }else{
           throw new ProductoNoExiste("Prodcuto no existe");
        }
    }
    /*
    public void eliminarSolicitud(String nombre,String cedulaVendedor) throws ProductoNoExiste {

        int posVendedor = obtenerPosicionVendedor(cedulaVendedor);
        int posProducto  = obtenerPosicionProducto(nombre, listaVendedores.get(posVendedor).getListaSolicitudes());

        Optional<Producto> s1 = listaVendedores.get(posVendedor).getListaSolicitudes().stream().filter(x -> x.getNombre().equals(nombre)).findFirst();

        if(s1.isPresent()){
            listaVendedores.get(posVendedor).getListaProductos().remove(posProducto);

        }else{
            throw new ProductoNoExiste("Solicitud no existe");
        }
    }
     */

    @Override
    public Producto obtenerProducto(String nombre,String cedulaVendedor) throws ProductoNoExiste {
        int posVendedor = obtenerPosicionVendedor(cedulaVendedor);
        Optional<Producto> p1 = listaVendedores.get(posVendedor).getListaProductos().stream().filter(x -> x.getNombre().equals(nombre)).findFirst();

        if(p1.isPresent()){
            return p1.get();
        }else{
            throw new ProductoNoExiste("Producto no existe");
        }
    }

    @Override
    public ArrayList<Producto> obtenerProductos(String cedulaVendedor) {
        int posVendedor = obtenerPosicionVendedor(cedulaVendedor);
        return listaVendedores.get(posVendedor).getListaProductos();
    }
    //Kevin Agrego
    public ArrayList<Solicitud> obtenerSolicitudes(String ceulaVendedor){
        int posVendedor = obtenerPosicionVendedor(ceulaVendedor);
        return listaVendedores.get(posVendedor).getListaSolicitudes();
    }



    public boolean verificarLogin(String usuario, String contrasenia) {

        boolean r = false;
        try {
            Vendedor v1 = obtenerVendedor(usuario);
            if(v1.getUsuario().getNombreUsuario().equals(usuario) && v1.getUsuario().getContrasenia().equals(contrasenia)){
                v1.getUsuario().setEstadoLogin(true);
                r = true;

            }

        } catch (VendedorNoExisteException e) {
            throw new RuntimeException(e);
        }

        return r;
    }

    public Optional<Vendedor> buscarVendedorLog(){
        return listaVendedores.stream().filter(x-> x.getUsuario().getEstadoLogin() == true).findFirst();
    }

    public void quitarEstadoLogin(){
        listaVendedores.stream().filter(x-> x.getUsuario().getEstadoLogin() == true).findFirst().get().getUsuario().setEstadoLogin(false);
    }
    //Chat
    public void iniciarChat(Vendedor vendedorSelecionado, Vendedor actual, String chat) {
        Chat chatVendedores = new Chat();

        chatVendedores.setVendedor(vendedorSelecionado);
        chatVendedores.setMiChat(chat);
    }



    public void agregarMeGustaProducto(Producto productoSeleccionado, Vendedor vendedorActual) {
        boolean encontroMegustaActual = false;
        //System.out.println("*********************************"+productoSeleccionado.getListaMeGustaProducto().size());
        try {
            if (productoSeleccionado.getListaMeGustaProducto().size() > 0) {
                for (MeGusta megustaActual : productoSeleccionado.getListaMeGustaProducto()) {
                    if (vendedorActual.equals(megustaActual.getVendedor())) {
                        productoSeleccionado.getListaMeGustaProducto().remove(megustaActual);
                        encontroMegustaActual = true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("causa"+e.getCause()+"Mensajee"+e.getMessage());
        }

        if (!encontroMegustaActual) {
            MeGusta nuevoMegusta = new MeGusta(productoSeleccionado, vendedorActual);
            productoSeleccionado.getListaMeGustaProducto().add(nuevoMegusta);
        }
    }
    public int contarMegustasProducto(Producto productoSeleccionado){
        int contador = 0;
        for(Vendedor v: listaVendedores){
            for (Producto p:v.getListaProductos()
            ) {
                if (p.getListaMeGustaProducto().size() > 0) {
                    for (MeGusta megustaActual : p.getListaMeGustaProducto()) {
                        if (productoSeleccionado.equals(megustaActual.getProducto())) {
                            contador++;
                        }
                    }
                }
            }
        }




        return contador;
    }
    public int contarMegustas(Vendedor vendedorActual){
        int contador = 0;
        for(Vendedor v: listaVendedores){
            for (Producto p:v.getListaProductos()
                 ) {
                if (p.getListaMeGustaProducto().size() > 0) {
                    for (MeGusta megustaActual : p.getListaMeGustaProducto()) {
                        if (vendedorActual.equals(megustaActual.getVendedor())) {
                            contador++;
                        }
                    }
                }
            }
        }

        return contador;
    }

    public Comentario crearComentario(Vendedor vendedorEnviado, Producto productoComentado, String nombre, String identificacion, String comentario){
        Comentario c1 = new Comentario( vendedorEnviado, productoComentado, nombre, identificacion, comentario);

        return c1;
    }





        //Obtener cedulaVendedor de la venta
        //actualizarProducto(productoCompra.getNombre(), productoCompra.getImagen(), productoCompra.getPrecio(), Estado.VENDIDO, );



    }

