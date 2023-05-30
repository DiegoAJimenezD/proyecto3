package co.edu.uqvirtual.markerplace.modelo;

import java.io.Serializable;

public class MeGusta implements Serializable {
    private static final long serialVersionUID = 1l;
    private boolean meGusta;
    private Producto producto;
    private Vendedor vendedor;

    public MeGusta() {
    }

    public MeGusta(Producto producto, Vendedor vendedor) {
        this.meGusta = true;
        this.producto = producto;
        this.vendedor = vendedor;
    }

    public boolean isMeGusta() {
        return meGusta;
    }

    public void setMeGusta(boolean meGusta) {
        this.meGusta = meGusta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
}
