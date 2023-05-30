package co.edu.uqvirtual.markerplace.modelo;

import java.io.Serializable;

public class Administrador extends Persona implements Serializable {

    private static final long serialVersionUID = 1l;
    private Usuario usuario;
    boolean login;

    public Administrador() {
    }

    public Administrador(String nombre, String apellido, String cedula, Usuario usuario) {
        super(nombre, apellido, cedula);
        this.usuario = usuario;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
