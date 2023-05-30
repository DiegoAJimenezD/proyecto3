package co.edu.uqvirtual.markerplace.persistence;




import co.edu.uqvirtual.markerplace.modelo.*;


import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Persistencia {

	public static final String RUTA_ARCHIVO_MARKEPLACE_BINARIO_DAT = "C://td//MarketPlaceBinario.dat";
	public static final String RUTA_ARCHIVO_MARKETPLACE_XML = "C://td//MarketPlace.xml";
	public static final String RUTA_ARCHIVO_LOG = "C://td//persistencia//log//marketPlace_Log.txt";
	;
	public static final String RUTA_ARCHIVO_VENDEDOR = "C://td//persistencia//archivos//ArchivoVendedor.txt";

	public static final String RUTA_ARCHIVO_RESPALDO_VENDEDOR = "C://td//persistencia//respaldo//ArchivoVendedorRespaldo.txt.txt";

	public static final String RUTA_ARCHIVO_MARKETPLACE_XML_RESPALDO = "C://td//MarketPlaceRespaldo.xml";

	public static final String RUTA_ARCHIVO_MARKEPLACE_BINARIO_DAT_RESPALDO = "C://td//MarketPlaceBinariorRespaldo.dat";


	//public static final String RUTA_ARCHIVO_PRODUCTO = "C://td//persistencia//archivos//ArchivoProducto.txt";
	public static final String RUTA_ARCHIVO_PRODUCTO = "C://td//persistencia//archivos//ArchivoProducto";





	public static void guardarArchivoEstadisticas(String nombreUsuario, String fechaMomento, String rutaElegida, String contenido ){

		String nombreArchivo = "reporte_"+fechaMomento+".txt";
		String archivoEstructura = "";
		try {
			FileWriter writer = new FileWriter(rutaElegida+""+nombreArchivo);

			writer.write("Reporte de Listado de Clientes" + "\n");
			writer.write("Fecha: " + fechaMomento + "\n");
			writer.write("Reporte realizado por: " + nombreUsuario + "\n\n");
			writer.write("Informaci�n del reporte:\n" + contenido + "\n");
			writer.close();
			System.out.println("El archivo se ha exportado correctamente en la siguiente ruta: " + rutaElegida);
		} catch (IOException e) {
			System.out.println("Ha ocurrido un error al exportar el archivo: " + e.getMessage());
		}

	}


	public static void copiarArchivoRespaldoVendedor() {
		ArchivoUtil.copiarArchivoRespaldo(RUTA_ARCHIVO_VENDEDOR, RUTA_ARCHIVO_RESPALDO_VENDEDOR);
	}
	public static void copiarArchivoRespaldoProducto(){

	}
	public static void copiarArchivoRespaldoXml(){
		ArchivoUtil.copiarArchivoRespaldo(RUTA_ARCHIVO_MARKETPLACE_XML,RUTA_ARCHIVO_MARKETPLACE_XML_RESPALDO );


	}
	public static void copiarArchivoRespaldoBinario(){
		ArchivoUtil.copiarArchivoBinario(RUTA_ARCHIVO_MARKEPLACE_BINARIO_DAT,RUTA_ARCHIVO_MARKEPLACE_BINARIO_DAT_RESPALDO);

	}
	
//	----------------------LOADS------------------------
	/**
	 * GENERAR REGISTRO
	 * @param mensajeLog
	 * @param nivel
	 * @param accion
	 */
	public static  void guardaRegistroLog(String mensajeLog, int nivel, String accion) {
		ArchivoUtil.guardarRegistroLog(mensajeLog, nivel, accion, RUTA_ARCHIVO_LOG);
	}
//	----------------------SAVES------------------------
// ------------------------------------SERIALIZACI�N(BINARIO) y XML
	/**
	 * EXPORTAR VENDEDOR EN ARCHIVO DE TEXTO
	 * @param listVendedores
	 * @throws IOException 
	 */
	public static void guardarVendedorTxt(ArrayList<Vendedor> listVendedores) throws IOException {
		String contenido = "";
		int cont = 1;
		for (Vendedor vendedor : listVendedores) {

			guardarProductoTxt(vendedor.getListaProductos(), cont);


			contenido +=vendedor.getNombre()+">@@<"+vendedor.getApellido()+">@@<"+vendedor.getCedula()+">@@<"+
					vendedor.getUsuario().getNombreUsuario()+">@@<"+vendedor.getUsuario().getContrasenia()+">@@<"+vendedor.getListaProductos()+">@@<"+vendedor.getListaSolicitudes()+
					">@@<"+vendedor.getListaSolicitudesEnivadas()+">@@<"+vendedor.getListaAliados()+">@@<"+vendedor.getListaProdutosSugeridos()+"\n";
			cont+=1;
		}
		
		ArchivoUtil.guardarArchivo(RUTA_ARCHIVO_VENDEDOR, contenido, false);

	}
	/**
	 * EXPORTAR PRODUCTO EN ARCHIVO DE TEXTO
	 * @param listProductos
	 * @throws IOException
	 */
	public static void guardarProductoTxt(ArrayList<Producto> listProductos, int numPosicion) throws IOException {
		String contenido = "";

		for (Producto producto : listProductos) {
			contenido +=producto.getNombre()+">@@<"+producto.getImagen()+">@@<"+producto.getPrecio()+">@@<"+producto.getEstado()+
					">@@<"+producto.getListaComentarioProductos()+">@@<"+producto.getListaMeGustaProducto()+">@@<"+ "\n";

		}
		
		ArchivoUtil.guardarArchivo(RUTA_ARCHIVO_PRODUCTO+numPosicion+".txt", contenido, false);
		//ArchivoUtil.guardarArchivo(RUTA_ARCHIVO_PRODUCTO+numPosicion+".txt", contenido, true);

	}

	public static ArrayList<Vendedor> cargarVendedores() throws IOException {
		ArrayList<Vendedor> listaVendedores = new ArrayList<>();
		ArrayList<String> contenido = ArchivoUtil.leerArchivo(RUTA_ARCHIVO_VENDEDOR);
		String linea="";
		int cont = 1;
		for (int i = 0; i < contenido.size(); i++)
		{
			linea = contenido.get(i);
			Vendedor vendedor = new Vendedor();
			Usuario usuario = new Usuario();
			vendedor.setNombre(linea.split(">@@<")[0]);
			vendedor.setApellido(linea.split(">@@<")[1]);
			vendedor.setCedula(linea.split(">@@<")[2]);

			usuario.setNombreUsuario(linea.split(">@@<")[3]);

			usuario.setContrasenia(linea.split(">@@<")[4]);

			//FALANTAN LAS LISTAS
			vendedor.setUsuario(usuario);


			vendedor.setListaProductos(cargarProductos(cont));


			listaVendedores.add(vendedor);
			cont+=1;
		}


		return listaVendedores;
	}

	public static ArrayList<Producto> cargarProductos(int numPosicion) throws FileNotFoundException, IOException {

		ArrayList<Producto> productos =new ArrayList<>();

		ArrayList<String> contenido = ArchivoUtil.leerArchivo(RUTA_ARCHIVO_PRODUCTO+numPosicion+".txt");
		String linea="";

		for (int i = 0; i < contenido.size(); i++)
		{
			linea = contenido.get(i);
			Producto producto = new Producto();
			producto.setNombre(linea.split(">@@<")[0]);

			producto.setImagen(linea.split(">@@<")[1]);
			producto.setPrecio(linea.split(">@@<")[2]);
			//FALTAN LAS LISTAS
			producto.setEstado(colocarValorEstadoProducto(linea.split(">@@<")[3]));

			productos.add(producto);



		}
		return productos;


	}
	public static Estado colocarValorEstadoProducto(String tipoEstado){
		switch (tipoEstado){
			case "PUBLICADO":
				return Estado.PUBLICADO;


			case"CANCELADO":
				return Estado.CANCELADO;

			case"VENDIDO":
				return Estado.VENDIDO;

		}

		return null;
	}






	/**
	 * CARGAR RECURSO BINARIO
	 * @return
	 */
	public static MarketPlace cargarRecursomarkBinario() {
		MarketPlace mark = null;
		try {
			mark = (MarketPlace)ArchivoUtil.cargarRecursoSerializado(RUTA_ARCHIVO_MARKEPLACE_BINARIO_DAT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mark;
	}
	/**
	 * GUARDAR RECURSO BINARIO
	 * @param mark
	 */
	public static  void guardarRecursoMarkBinario(MarketPlace mark) {
		try {
			ArchivoUtil.salvarRecursoSerializado(RUTA_ARCHIVO_MARKEPLACE_BINARIO_DAT, mark);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	/**
	 * CARGAR RECURSO BINARIO
	 * @return
	 */
	public static MarketPlace cargarRecursoMarketplaceXML() {
		Object object = null;
		MarketPlace mark = null;
		
		
		try {
			object = ArchivoUtil.cargarRecursoSerializadoXML(RUTA_ARCHIVO_MARKETPLACE_XML);
			mark = (MarketPlace) object;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mark;
	}

	/**
	 * GUARDAR RECURSO BINARIO EN XML
	 * @param mark
	 */
	public static  void guardarRecursoMarketplaceXML(MarketPlace mark) {
		try {
			
			ArchivoUtil.salvarRecursoSerializadoXML(RUTA_ARCHIVO_MARKETPLACE_XML, mark);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
