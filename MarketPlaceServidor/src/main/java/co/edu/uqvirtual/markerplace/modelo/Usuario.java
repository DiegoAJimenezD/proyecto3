package co.edu.uqvirtual.markerplace.modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1l;
    private String nombreUsuario;
    private String contrasenia;
    private Boolean estadoLogin ;

    public Usuario() {
    }

    public Usuario(String nombreUsuario, String contrasenia) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.estadoLogin = false;
    }

    public Boolean getEstadoLogin() {
        return estadoLogin;
    }

    public void setEstadoLogin(Boolean estadoLogin) {
        this.estadoLogin = estadoLogin;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
