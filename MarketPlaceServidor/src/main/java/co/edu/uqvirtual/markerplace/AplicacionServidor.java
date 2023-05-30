package co.edu.uqvirtual.markerplace;

import co.edu.uqvirtual.markerplace.server.Servidor;

import java.io.IOException;

public class AplicacionServidor {
    static Servidor miServidor;


    public static void main(String args[]) throws IOException
    {

        miServidor=new Servidor();

        try {
            miServidor.runServer();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
