package co.edu.uqvirtual.markerplace.modelo;

import co.edu.uqvirtual.markerplace.controllers.ChatViewController;

import java.io.Serial;
import java.io.Serializable;

public class Chat implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private  Vendedor vendedor;
    private String miChat;

    //Constructores
    public Chat(){
        super();
    }
    public Chat(Vendedor vendedor, String miChat){
        super();
        this.vendedor = vendedor;
        this.miChat = miChat;
    }
    public Vendedor getVendedor() {
        return vendedor;
    }
    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
    public String getMiChat() {
        return miChat;
    }

    public void setMiChat(String miChat){
        this.miChat = miChat;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    /**
     * toString
     */
    @Override
    public String toString() {
        return "ChatVendedores [vendedor2=" + vendedor + ", miChat=" + miChat + "]";
    }
    /*

    private Vendedor mensaje;
    String VendedorAsociado;


    public Chat() {
    }

    public String getVendedorAsociado() {
        return VendedorAsociado;
    }

    public void setVendedorAsociado(String vendedorAsociado) {
        VendedorAsociado = vendedorAsociado;
    }

    public Chat(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;

     */

}
