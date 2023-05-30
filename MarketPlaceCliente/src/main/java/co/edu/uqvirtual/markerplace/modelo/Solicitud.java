package co.edu.uqvirtual.markerplace.modelo;

import java.io.Serializable;

public class Solicitud implements Serializable {
    private static final long serialVersionUID = 1l;
    private Vendedor vendedorEnviado;
    private Vendedor VendedorResivido;
    private String nombre;
    private String cedula;
    private boolean respuesta;



    public Solicitud(String nombre, String cedula, boolean respuesta) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.respuesta = respuesta;
    }
    public Solicitud() {
        super();
    }
    public Vendedor getVendedorEnviado(){
        return vendedorEnviado;
    }
    public void setVendedorEnviado(Vendedor vendedorEnviado){
        this.vendedorEnviado = vendedorEnviado;
    }
    public Vendedor getVendedorResivido() {
        return VendedorResivido;
    }

    public void setVendedorResivido(Vendedor vendedorResivido) {
        this.VendedorResivido = vendedorResivido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public boolean isRespuesta() {
        return respuesta;
    }

    public void setRespuesta(boolean respuesta) {
        this.respuesta = respuesta;
    }



}
