package co.edu.uqvirtual.markerplace.server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor
{
	HiloServidor hiloServidorCliente;
	boolean escuchando=true;
	String idAplicacion;
	ServerSocket serverComunicacion=null;//server para comunicacion
	ServerSocket serverTransferenciaObjeto=null;//server para enviar objeto de persisitencia

	DataInputStream flujoEntradaComunicacion=null;
	DataOutputStream flujoSalidadComunicacion=null;
	ObjectOutputStream flujoSalidaObjeto=null;
	ObjectInputStream flujoEntradaObjeto=null;

	public ArrayList<HiloServidor> aplicacionesClientesActivas=new ArrayList<HiloServidor>();	

	public void runServer() throws Exception
	{

		escuchando=true;
		try{
			serverComunicacion=new ServerSocket(8081);
			serverTransferenciaObjeto=new ServerSocket(8082);

			while(escuchando)
			{
				Socket socketComunicacion = null;//para comunicacion
				Socket socketTransferenciaObjetos=null; //para enviar archivo de persisitencia

				try {
					System.out.println("Servidor esperando conexion del cliente");
					socketComunicacion = serverComunicacion.accept();
					socketTransferenciaObjetos = serverTransferenciaObjeto.accept();
					System.out.println("Conexi�n establecidad");

					flujoEntradaComunicacion=new DataInputStream(socketComunicacion.getInputStream());
					flujoSalidadComunicacion=new DataOutputStream(socketComunicacion.getOutputStream());
					flujoSalidaObjeto = new ObjectOutputStream(socketTransferenciaObjetos.getOutputStream());
					flujoEntradaObjeto = new ObjectInputStream(socketTransferenciaObjetos.getInputStream());

					iniciarHiloServidorCliente();

				} 
				catch (IOException e)
				{
					System.out.println(e.getMessage());
				}
			}

		}catch(IOException e){
			throw new Exception ("Error en el servidor");
		}
	}



	private void iniciarHiloServidorCliente() throws IOException {

		hiloServidorCliente = new HiloServidor();
		hiloServidorCliente.inicializarConexion(flujoEntradaComunicacion,flujoSalidadComunicacion,flujoSalidaObjeto,flujoEntradaObjeto,this, getIdAplicacion());
		hiloServidorCliente.start();

	}



	public boolean isListening() {
		return escuchando;
	}
	public void setListening(boolean listening) {
		this.escuchando = listening;
	}
	public HiloServidor getUser() {
		return hiloServidorCliente;
	}
	public void setUser(HiloServidor user) {
		this.hiloServidorCliente = user;
	}



	public ArrayList<HiloServidor> getAplicacionesClientesActivas() {
		return aplicacionesClientesActivas;
	}



	public void setAplicacionesClientesActivas(ArrayList<HiloServidor> aplicacionesClientesActivas) {
		this.aplicacionesClientesActivas = aplicacionesClientesActivas;
	}


	public String getIdAplicacion()
	{
		return idAplicacion;
	}

	public void setidAplicacion(String idAplicacion)
	{
		this.idAplicacion=idAplicacion;
	}


}



