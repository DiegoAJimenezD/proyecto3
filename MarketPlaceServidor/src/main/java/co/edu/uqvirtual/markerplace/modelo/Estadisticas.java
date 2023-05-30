package co.edu.uqvirtual.markerplace.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Estadisticas extends Vendedor implements Serializable {

    private static final long serialVersionUID = 1l;
    private ArrayList<Vendedor> listaVendedoresEstadisticas = new ArrayList<>();


    public Estadisticas() {

    }

    public Estadisticas(String nombre, int cantidadProductos) {
        listaVendedoresEstadisticas = new ArrayList<>();
    }

    public ArrayList<Vendedor> getListaVendedoresEstadisticas() {
        return listaVendedoresEstadisticas;
    }

    public void setListaVendedoresEstadisticas(ArrayList<Vendedor> listaVendedoresEstadisticas) {
        this.listaVendedoresEstadisticas = listaVendedoresEstadisticas;
    }
}
