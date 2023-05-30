package co.edu.uqvirtual.markerplace.server;






import co.edu.uqvirtual.markerplace.exceptions.VendedorNoExisteException;
import co.edu.uqvirtual.markerplace.modelo.Chat;
import co.edu.uqvirtual.markerplace.modelo.Estado;
import co.edu.uqvirtual.markerplace.modelo.MarketPlace;
import co.edu.uqvirtual.markerplace.modelo.Producto;
import co.edu.uqvirtual.markerplace.modelo.Solicitud;
import co.edu.uqvirtual.markerplace.modelo.Usuario;
import co.edu.uqvirtual.markerplace.modelo.Vendedor;
import co.edu.uqvirtual.markerplace.persistence.Persistencia;

import java.io.*;


public class HiloServidor extends Thread
{
     String idAplicacion;
     Servidor miServidor;
     
     DataInputStream flujoEntradaComunicacion=null;
     DataOutputStream flujoSalidadComunicacion=null;
     ObjectOutputStream flujoSalidaObjeto=null;
     ObjectInputStream flujoEntradaObjeto=null;
     DataOutputStream dos;
     byte[] byteArray;
     int in;
     File localFileServer;
     int opcion=0;
     
     MarketPlace marketPlace;

	 boolean loginAdmin = true;
	 boolean loginRespuesta ;
	Usuario usuarioLogin;




     public HiloServidor()
     {
     }
     
 	
	public void inicializarConexion(DataInputStream flujoEntradaComunicacion,DataOutputStream flujoSalidadComunicacion, ObjectOutputStream flujoSalidaObjeto,
			ObjectInputStream flujoEntradaObjeto, Servidor servidor, String idAplicacion) {
		
        this.miServidor=servidor;
        this.idAplicacion = idAplicacion;
		this.flujoEntradaComunicacion = flujoEntradaComunicacion;
		this.flujoSalidadComunicacion = flujoSalidadComunicacion;
		this.flujoSalidaObjeto = flujoSalidaObjeto;
		this.flujoEntradaObjeto = flujoEntradaObjeto;

	}
     
     public void run()
     {


    	 try
    	 {
    		 opcion=flujoEntradaComunicacion.readInt();
    		 System.out.println(opcion);
    		 
    		 switch(opcion)
    		 {
    		 case 1://Solicitud de enviar el objeto  de persistencia

    			 try {
    				 enviarInformacionPersistencia();

    			 } catch (Exception e) {
    				 // TODO Auto-generated catch block
    				 e.printStackTrace();
    			 }

    			 break;
    		 case 2:////Solicitud de guardar el objeto  de persistencia
    			 guardarInformacionPersistencia();
    			 break;

    		 case 3: // Verificar Login
				 usuarioLogin = (Usuario) flujoEntradaObjeto.readObject();
				 if(loginAdmin){
					 verificarLoginAdmin();
				 }
				 if(!loginAdmin){

					 verificarLoginVendedor();
				 }
				 System.out.println("Se verifica login");
			     //enviarRespuesta();
				 System.out.println("Se envia la repuesta Login");
				 //verificarLogin();
    			 break;

				 case 4:
					 enviarRespuesta();
					 System.out.println("Se envia la repuesta Login");
					 break;
    		 }
    		 
    	 }catch (Exception e) {
    		 // TODO: handle exception
    	 }

     }

     public void verificarLoginAdmin() throws IOException, ClassNotFoundException, VendedorNoExisteException {
		 marketPlace = Persistencia.cargarRecursoMarketplaceXML();


		 if(marketPlace.obtenerAdmin(usuarioLogin.getNombreUsuario(),usuarioLogin.getContrasenia())){
			 loginAdmin = true;
			 loginRespuesta = true;

			 flujoSalidadComunicacion.writeBoolean(loginRespuesta);
		 }else{
			 loginRespuesta = false;
			 loginAdmin = false;
			 flujoSalidadComunicacion.writeBoolean(loginRespuesta);
		 }





	 }
	 public void enviarRespuesta() throws IOException {

		 flujoSalidadComunicacion.writeBoolean(loginRespuesta);

	 }
	public void verificarLoginVendedor() throws IOException, ClassNotFoundException, VendedorNoExisteException {


		 loginAdmin = true;
			if(marketPlace.verificarVendedorExistente(usuarioLogin.getNombreUsuario())){
				if(marketPlace.verificarLogin(usuarioLogin.getNombreUsuario(), usuarioLogin.getContrasenia())){


					loginRespuesta = true;
				}else{
					loginRespuesta = false;
				}
			}else{
				loginRespuesta = false;

			}
		flujoSalidadComunicacion.writeBoolean(loginRespuesta);



	}
     private void enviarInformacionPersistencia() throws IOException,Exception {

    	 marketPlace = Persistencia.cargarRecursoMarketplaceXML();
		 //System.out.println(marketPlace.getAdmin().getUsuario());

		 //inicializarDatos();
    	 if(marketPlace == null)
    		 return;


    	 System.out.println("Se envio la informaci�n del marketPlace");
    	 flujoSalidaObjeto.writeObject(marketPlace);
     }
     
     
     private void guardarInformacionPersistencia() throws IOException,Exception {

    	 marketPlace =  (MarketPlace) flujoEntradaObjeto.readObject();
		 //inicializarDatos();
    	 Persistencia.guardarRecursoMarketplaceXML(marketPlace);
    	 System.out.println("Se guardo la informaci�n del marketPlace");
     }
	public void inicializarDatos() {

		marketPlace = new MarketPlace();

		Vendedor v1 = new Vendedor("Diego", "Jimenez", "1234", new Usuario("1234", "1234"));
		Vendedor v2 = new Vendedor("Kevin", "Payanene", "321", new Usuario("321", "321"));
		Vendedor v3= new Vendedor("Valeria", "Mendoza", "4321", new Usuario("4321", "4321"));

		v1.getListaChat().add(new Chat(v2, "Holaaa"));

		v2.getListaProductos().add(new Producto("Soda L", "Null", "2000", Estado.PUBLICADO,"fechaMomento" ));
		v1.getListaProductos().add(new Producto("Menta L", "Null", "120", Estado.PUBLICADO, "fechaMomento"));


		Solicitud s1 =  new Solicitud("Kevin", "321", false);
		v1.getListaSolicitudes().add(s1);
		s1.setVendedorEnviado(v2);
		v1.getListaAliados().add(v2);

		//v1.getListaAliados().add(v2);



		marketPlace.getListVendedores().add(v1);
		marketPlace.getListVendedores().add(v2);
		marketPlace.getListVendedores().add(v3);


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