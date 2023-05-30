package co.edu.uqvirtual.markerplace.controllers;

import co.edu.uqvirtual.markerplace.exceptions.DatosNulosException;
import co.edu.uqvirtual.markerplace.exceptions.VendedorNoExisteException;
import co.edu.uqvirtual.markerplace.modelo.*;
import co.edu.uqvirtual.markerplace.persistence.Persistencia;
import co.edu.uqvirtual.markerplace.services.IModelFactoryService;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class ModelFactoryController implements IModelFactoryService, Runnable {
    MarketPlace marketPlace;
    Vendedor vendedorActual;
    Producto productoActualComentario;

    Thread hiloServicio1_guardarResourceXml;
    Thread hiloServicio2_guardarRegistroLog;

    Thread hiloServicio3_guardarArchivo;
    BoundedSemaphore semaphore = new BoundedSemaphore(2);

    String mensaje;
    int nivel;
    String accion;

    Vendedor[] vendedores = new Vendedor[11];
    private Vendedor vendedorAutenticado;
    String fechaMomento;
    boolean respuestaAdmin ;
    boolean respuestaVendedor;



    public boolean isRespuestaAdmin() {
        return respuestaAdmin;
    }

    public boolean isRespuestaVendedor() {
        return respuestaVendedor;
    }

    public void setRespuestaAdmin(boolean respuestaAdmin) {
        this.respuestaAdmin = respuestaAdmin;
    }

    public void setRespuestaVendedor(boolean respuestaVendedor) {
        this.respuestaVendedor = respuestaVendedor;
    }

    private Socket socketComunicacion = null;//para la comunicacion (entrada y salida)
    private Socket socketTransferenciaObjeto = null;//permite recibir el archivo
    private DataOutputStream flujoSalidaComunicacion = null;
    private DataInputStream flujoEntradaComunicacion;

    private String idInstanciaCliente = "";
    byte[] receivedData;
    int in;
    BufferedInputStream flujoEntradaArchivo;
    BufferedOutputStream flujoSalidadLocalArchivo;

    String filename = "";
    String rutaArchivoLocal = "src/resourcesServidor/";
    ObjectInputStream flujoEntradaObjeto;
    ObjectOutputStream flujoSalidaObjeto;




    private static class SingletonHolder {
        // El constructor de Singleton puede ser llamado desde aquí al ser protected
        private final static ModelFactoryController eINSTANCE = new ModelFactoryController();
    }
    public static ModelFactoryController getInstance() {
        return SingletonHolder.eINSTANCE;
    }

    public ModelFactoryController() {
        //Respaldos

        System.out.println("llamado....");

        try {
            crearConexion();

            solicitarInformacionPersistencia();
            //System.out.println("Hola1");
            leerObjetoPersistenciaTransferido();
            //System.out.println("Hola2");




        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        inicializarDatos();
//        guardarResourceXML();

        //111guardarRespaldosArchivos();



        //1. inicializar datos y luego guardarlo en archivos
          //inicializarSalvarDatos();
//        Persistencia.guardarRecursoMarketplaceXML(marketPlace);


        //2. Cargar los datos de los archivos
        //cargarDatosDesdeArchivos();

        //3. Guardar y Cargar el recurso serializable binario
        //inicializarDatos();
        //guardarResourceBinario();
        //cargarResourceBinario();


        //4. Guardar y Cargar el recurso serializable XML
         //guardarResourceXML();
        //cargarResourceXML();

        //Siempre se debe verificar si la raiz del recurso es null

        asignarDatosArregloVendedores();
        if(marketPlace == null){
            System.out.println("nada");
            //inicializarDatos();
            //guardarResourceXML();
        }
        //registrarAccionesSistema("Datos cargados", 1, "Cargar datos");
        //inicializarDatos();
    }

    public void guardarRespaldosArchivos(){
        Persistencia.copiarArchivoRespaldoXml();
        Persistencia.copiarArchivoRespaldoBinario();
        Persistencia.copiarArchivoRespaldoVendedor();
    }
    public void guardarDatosArchivos() throws IOException {

        Persistencia.guardarVendedorTxt(getMarketPlace().getListVendedores());
        Persistencia.copiarArchivoRespaldoVendedor();
        //Persistencia.guardarRecursoMarketplaceXML(marketPlace);
        //Persistencia.guardarRecursoMarkBinario(marketPlace);

        asignarDatosArregloVendedores();




    }
    public void inicializarDatos() {

        marketPlace = new MarketPlace();

        Vendedor v1 = new Vendedor("Diego", "Jimenez", "1234", new Usuario("1234", "1234"));
        Vendedor v2 = new Vendedor("Kevin", "Payanene", "321", new Usuario("321", "321"));
        Vendedor v3= new Vendedor("Valeria", "Mendoza", "4321", new Usuario("4321", "4321"));

        v1.getListaChat().add(new Chat(v2, "Holaaa"));
        obtenerFechaMomento();
        v2.getListaProductos().add(new Producto("Soda L", "Null", "2000", Estado.PUBLICADO,fechaMomento ));
        v1.getListaProductos().add(new Producto("Menta L", "Null", "120", Estado.PUBLICADO, fechaMomento));


        Solicitud s1 =  new Solicitud("Kevin", "321", false);
        v1.getListaSolicitudes().add(s1);
        s1.setVendedorEnviado(v2);
        v1.getListaAliados().add(v2);

        //v1.getListaAliados().add(v2);



        marketPlace.getListVendedores().add(v1);
        marketPlace.getListVendedores().add(v2);
        marketPlace.getListVendedores().add(v3);
        registrarAccionesSistema("incio sesion", 1, "inicializar Datos");

    }

    private void cargarResourceXML(){
        marketPlace = Persistencia.cargarRecursoMarketplaceXML();
    }
    private void guardarResourceXML() {
        hiloServicio1_guardarResourceXml = new Thread(this);
        hiloServicio1_guardarResourceXml.start();

        //Quitar
        //Persistencia.guardarRecursoMarketplaceXML(marketPlace);

    }

    private void cargarResourceBinario() {
        marketPlace = Persistencia.cargarRecursomarkBinario();

    }

    private void guardarResourceBinario() {

        inicializarDatos();
        Persistencia.guardarRecursoMarkBinario(marketPlace);

    }
    public void guardarArchivosTexto() {
        hiloServicio3_guardarArchivo = new Thread(this);
        hiloServicio3_guardarArchivo.start();
    }

    private void inicializarSalvarDatos() {
        inicializarDatos();
        try {
            Persistencia.guardarVendedorTxt(getMarketPlace().getListVendedores());
            Persistencia.copiarArchivoRespaldoVendedor();

            //Persistencia.guardarProductoTxt(getBanco().getListaEmpleados());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private void cargarDatosDesdeArchivos() {
        this.marketPlace = new MarketPlace();
        try {
            ArrayList<Vendedor> listaVendedores = new ArrayList<>();
            listaVendedores = Persistencia.cargarVendedores();
            getMarketPlace().getListVendedores().addAll(listaVendedores);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void registrarAccionesSistema(String mensaje, int nivel, String accion) {
        this.mensaje=mensaje;
        this.nivel=nivel;
        this.accion=accion;
        hiloServicio2_guardarRegistroLog = new Thread(this);
        hiloServicio2_guardarRegistroLog.start();
        //Persistencia.guardaRegistroLog(mensaje, nivel, accion);
    }
    public MarketPlace getMarketPlace() {
        return this.marketPlace;
    }

    public void setMarketPlace(MarketPlace marketPlace) {
        this.marketPlace = marketPlace;
    }

    public Vendedor getVendedorAutenticado() {
        return vendedorAutenticado;
    }

    public void setVendedorAutenticado(Vendedor vendedorAutenticado) {
        this.vendedorAutenticado = vendedorAutenticado;
    }

    public Vendedor getVendedorActual() {
        return vendedorActual;
    }

    public void setVendedorActual(Vendedor vendedorActual) {
        this.vendedorActual = vendedorActual;
    }
    public Producto getProductoActualComentario(){
        return productoActualComentario;
    }
    public void setProductoActualComentario(Producto productoActualComentario){
        this.productoActualComentario = productoActualComentario;
    }
//    @Override
//    public void run() {
//        Thread hiloActual = Thread.currentThread();
//
//
//
//        if(hiloActual == hiloServicio1_guardarResourceXml){
//
//            try {
//                crearConexion();
//                solicitarGuardarPersistencia();
//                enviarObjetoPersistenciaTransferido();
//                //this.getMarketPlace().getAdmin().setLogin(false);
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//
//
//            //Persistencia.guardarRecursoMarkBinario(marketPlace);
//            liberarSemaforo();
//
//        }
//
//
//
//
//    }

    @Override
    public void run() {
        Thread hiloActual = Thread.currentThread();


        try {
            semaphore.ocupar();

        }catch (InterruptedException e){
            e.printStackTrace();
        }
        if(hiloActual == hiloServicio1_guardarResourceXml){

            try {
                crearConexion();
                solicitarGuardarPersistencia();
                enviarObjetoPersistenciaTransferido();
                //this.getMarketPlace().getAdmin().setLogin(false);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }



            //Persistencia.guardarRecursoMarkBinario(marketPlace);
            liberarSemaforo();
            liberarSemaforo();

        }

        if(hiloActual == hiloServicio2_guardarRegistroLog){
            Persistencia.guardaRegistroLog(mensaje, nivel, accion);
            liberarSemaforo();
            liberarSemaforo();

        }


    }

    private void liberarSemaforo() {
        try {
            semaphore.liberar();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void salvarDatos() {

//		guardarResourceBinario();
        guardarResourceXML();

    }


    /**
     *
     * Metodos de Vendedor
     *
     */
    public void asignarDatosArregloVendedores(){

        ArrayList<Vendedor> vendedorArrayList = obtenerVendedores();
        for (Vendedor v:vendedorArrayList
        ) {
            boolean bandera= true;
            if(!vendedorRepetido(v)){
                for(int i = 1; i < vendedores.length && bandera;i++){
                    if(vendedores[i] == null){

                        vendedores[i] = v;
                        bandera = false;
                    }
                }
            }
        }

    }
    public boolean vendedorRepetido(Vendedor vendedor) {

        for (Vendedor v : vendedores) {
            if(v != null){
                if (v.getNombre().equals(vendedor.getNombre())) {
                    return true;
                }
            }

        }
        return false;
    }

    public void eliminarVendedorArreglo(String cedula){
        for(int i = 0; i< vendedores.length;i++){
            if(vendedores[i] != null){
                if(vendedores[i].getCedula().equals(cedula)){
                    vendedores[i] = null;
                }
            }
        }
    }




    public int obtenerPosicionVendedorLog(){
        int r = 1;
        for(int i = 1;i < vendedores.length;i++){
            if(vendedores[i] != null){
                if(vendedorActual != null){
                    if(vendedores[i].getUsuario().equals(vendedorActual.getUsuario())){
                        return r;
                    }else{
                        r+=1;
                    }
                }
            }
        }

        if(marketPlace.getAdmin().isLogin()){
            r = 0;
        }


        return r;
    }
    public Optional<Vendedor> buscarVendedorLog() {
        return getMarketPlace().buscarVendedorLog();
    }

    public int obtenerPosicionLog() {
        return obtenerPosicionVendedorLog();
    }

    public boolean verificarLogin(String usuario, String contrasenia) {
        return this.getMarketPlace().verificarLogin(usuario, contrasenia);
    }
    public Vendedor loginVendedor(String nombre) throws VendedorNoExisteException {
        return this.getMarketPlace().obtenerVendedorLogin(nombre);
    }

    public void eliminarSolicitud(String cedula)  {
        try {
            this.getMarketPlace().eliminarSolicitud(cedula, vendedorActual);
            guardarDatosArchivos();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public int calcularChatsVendedores(String nombre1, String nombre2) {

        Vendedor vendedor1 = obtenerVendedorPorNombre(nombre1);
        Vendedor vendedor2 = obtenerVendedorPorNombre(nombre2);

        int contador = 0;

        for (Chat chat:vendedor1.getListaChat()
        ) {
            if(chat.getVendedor().getNombre().equals(vendedor2.getNombre())){
                contador++;
            }
        }
        return contador;

    }

    private Vendedor obtenerVendedorPorNombre(String nombre) {


        return this.getMarketPlace().obtenerVendedorLogin(nombre);
    }
    public void quitarLoginVendedor(){
        this.getMarketPlace().quitarEstadoLogin();
    }
    public int retornarCantidadMeGustas(){
        return this.getMarketPlace().contarMegustas(vendedorActual);
    }
    public ArrayList<Vendedor> obtenerVendedorAliado() {
        if(vendedorActual != null){
            return vendedorActual.getListaAliados();
        }else {
            return new ArrayList<>();
        }

    }


    /*************************************/


    /**
     * CRUD VENDEDORES
     * */

    @Override
    public void eliminarVendedor(String cedula)  {

        try {
            this.getMarketPlace().eliminarVendedor(cedula);
            eliminarVendedorArreglo(cedula);
            guardarDatosArchivos();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public boolean verificarVendedorExistente(String cedula) {
        try {
            return getMarketPlace().verificarVendedorExistente(cedula);

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
    @Override
    public Vendedor obtenerVendedor(String nombreUsuario) {
        try {
            vendedorAutenticado = getMarketPlace().obtenerVendedor(nombreUsuario);
            return vendedorAutenticado;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Vendedor crearVendedor(String nombre, String apellido, String cedula, String usuario, String contrasenia) {
        Vendedor vendedor = null;

        try {
            vendedor = marketPlace.crearVendedor(nombre, apellido, cedula, usuario, contrasenia);
            /*Persistencia.guardarVendedorTxt(getMarketPlace().getListVendedores());
            Persistencia.guardarRecursoMarketplaceXML(marketPlace);
            Persistencia.guardarRecursoMarkBinario(marketPlace);*/
            guardarDatosArchivos();

        } catch (DatosNulosException | IOException e1) {
            e1.printStackTrace();
        }

        return vendedor;
    }

    @Override
    public void actualizarVendedor(String nombre, String apellido, String cedula, String usuario, String contrasenia)  {

        try{
            this.getMarketPlace().actualizarVendedor(nombre,apellido,cedula,usuario,contrasenia);
            guardarDatosArchivos();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public ArrayList<Vendedor> obtenerVendedores() {
        return marketPlace.obtenerEmpleados();
    }





    /****************************************************************************/

    /**
     * METODOS PRODUCTOS
     * */

    public ArrayList<Producto> ordenarProductosPorFecha(ArrayList<Producto> productos){

        productos.sort(Comparator.comparing(Producto::obtenerFechaMomento));

        return productos;
    }



    public ArrayList<Producto> obtenerProductosVendedorActual() {
        if(vendedorActual != null){
            return vendedorActual.getListaProductos();
        }else {
        return new ArrayList<>();
    }

    }

    public ArrayList<Producto> obtenerTopProductos() {
        ArrayList<Producto> productosTotales = new ArrayList<>();

        for (Vendedor v:this.getMarketPlace().getListVendedores()
             ) {
            productosTotales.addAll(v.getListaProductos());
        }


        ArrayList<Producto> productosOrdenados = new ArrayList<>(productosTotales);

        productosOrdenados.sort((p1, p2) -> Integer.compare(p2.getListaMeGustaProducto().size(), p1.getListaMeGustaProducto().size()));


        return  productosOrdenados;
    }



    public String obtenerCantidadProductosPorFecha(LocalDateTime selectedDateTime1, LocalDateTime selectedDateTime2) {
        int cantidad = 0;
        ArrayList<Producto> productosTotales = new ArrayList<>();

        for (Vendedor v:this.getMarketPlace().getListVendedores()
        ) {
            productosTotales.addAll(v.getListaProductos());
        }

        for (Producto producto : productosTotales) {

            LocalDateTime fechaPublicacion = producto.obtenerFechaMomento();
            if (fechaPublicacion.isAfter(selectedDateTime1) && fechaPublicacion.isBefore(selectedDateTime2)) {
                cantidad++;
            }

        }



        return String.valueOf(cantidad);
    }

    public void agregarMegustaProducto(Producto productoSeleccionado) {
        this.getMarketPlace().agregarMeGustaProducto(productoSeleccionado, vendedorActual);
    }
    public ArrayList<Producto> obtenerProductosAliados() {
        ArrayList<Producto> listaProductos = new ArrayList<>();
        if(vendedorActual != null){
            ArrayList<Vendedor> obtenerVendedores = vendedorActual.getListaAliados();


            //ArrayList<Producto> listaProductos = new ArrayList<>();
            //listaProductos.addAll(vendedorActual.getListaProductos());
            for (Vendedor vendedor : obtenerVendedores) {
//            if(vendedor.getListaProductos().get(0) != null){
//                System.out.println(vendedor.getListaProductos().get(0));
//            }
                for (Producto p:vendedor.getListaProductos()
                ) {
                    if(p.getEstado() == Estado.PUBLICADO){
                        listaProductos.add(p);
                    }
                }
                //listaProductos.addAll(vendedor.getListaProductos());
            }
            //System.out.println(listaProductos.toString());
            return listaProductos;
        }else{
            return listaProductos;
        }

    }


    public void obtenerFechaMomento(){
        LocalDateTime now = LocalDateTime.now();

        // Obtener la fecha actual
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();

        // Obtener la hora actual
        int hour = now.getHour();
        int minute = now.getMinute();

        fechaMomento = year+"-"+month+"-"+day+"-"+hour+"-"+minute;
    }

    /*******************************************************************/

    /**
     * CRUD PRODUCTOS
     * */
    @Override
    public Producto crearProducto(String nombre, String imagen, String precio, Estado estado,String cedulaVendedor) {
        try{
            obtenerFechaMomento();
           Producto p1 =  this.getMarketPlace().crearProducto(nombre,imagen, precio, estado,cedulaVendedor, fechaMomento);
            guardarDatosArchivos();
            return p1;

        }catch (Exception e){
            e.printStackTrace();
            return null;

        }

    }

    @Override
    public void actualizarProducto(String nombre, String imagen, String precio, Estado estado,String cedulaVendedor) {
        try{
            this.getMarketPlace().actualizarProducto(nombre, imagen, precio, estado, cedulaVendedor);
            guardarDatosArchivos();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void eliminarProducto(String nombre,String cedulaVendedor) {
        try{
            this.getMarketPlace().eliminarProducto(nombre, cedulaVendedor);
            guardarDatosArchivos();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public Producto obtenerProducto(String nombre,String cedulaVendedor) {
        try{
            return this.getMarketPlace().obtenerProducto(nombre, cedulaVendedor);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean verificarProductoExistente(String nombre,String cedulaVendedor) {
        try{
            return this.getMarketPlace().verificarProductoExistente(nombre, cedulaVendedor);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public ArrayList<Producto> obtenerProductos(String cedulaVendedor) {
        //ArrayList<Producto> pl = this.getMarketPlace().obtenerProductos(cedulaVendedor);
        //pl.forEach(System.out::println);
        return this.getMarketPlace().obtenerProductos(cedulaVendedor);
    }

    public String obtenerMegustasProducto(Producto prodcuto){
        int r = 0;
       return String.valueOf(r);
    }



    /**************************************************************/



    /**
     * METODOS(mostrarMensajes, estadisticas)
     * */
    public void crearArchivoEstadisticas(String rutaElegida, String contenido){

        obtenerFechaMomento();
        Persistencia.guardarArchivoEstadisticas(this.getMarketPlace().getAdmin().getNombre(), fechaMomento, rutaElegida,contenido );
        //Persistencia.guardarArchivoEstadisticas(getMarketPlace().getAdmin().getNombre(), fechaMomento, rutaElegida,contenido );
    }




    void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {

        Alert alerta = new Alert(alertType);
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(contenido);
        alerta.showAndWait();

    }

    /*****************************/




    public void iniciarChat(String texto) {
        //marketPlace.iniciarChat(vendedorSeleccionado,vendedorActual,chat);
    }




    public ArrayList<Solicitud> obtenerSolicitud(String cedulaVendedor){
        return  this.getMarketPlace().obtenerSolicitudes(cedulaVendedor);
    }


    public void aceptarSolicitud(Solicitud solicitud) {
        try {
            this.getMarketPlace().aceptarSolicitud(solicitud, vendedorActual);
            guardarDatosArchivos();
        }catch (Exception e){
            e.printStackTrace();
        }

    }




    /**
     * SOCKETS
     * **/
    public void crearConexion() throws IOException,Exception
    {
        try {
            socketComunicacion = new Socket("localhost", 8081); //socket para enviar datos primitivos
            socketTransferenciaObjeto = new Socket("localhost", 8082);//sokcet para enviar y recibir objeto

            flujoEntradaComunicacion = new DataInputStream(socketComunicacion.getInputStream());
            flujoSalidaComunicacion = new DataOutputStream(socketComunicacion.getOutputStream());

            flujoEntradaObjeto = new ObjectInputStream(socketTransferenciaObjeto.getInputStream());
            flujoSalidaObjeto = new ObjectOutputStream(socketTransferenciaObjeto.getOutputStream());

        } catch (IOException e) {
            throw new Exception("\tError en el servidor");

        }
    }


    private void solicitarInformacionPersistencia() throws IOException {
        flujoSalidaComunicacion.writeInt(1);
        flujoSalidaComunicacion.close();
    }



    /**
     * Socktes
     * */
    private void leerObjetoPersistenciaTransferido() throws IOException, ClassNotFoundException {



        marketPlace =  (MarketPlace) flujoEntradaObjeto.readObject();
        System.out.println("objeto recibido");

        flujoEntradaObjeto.close();
    }


    private void solicitarGuardarPersistencia() throws IOException {

        flujoSalidaComunicacion.writeInt(2);
        flujoSalidaComunicacion.close();

    }

    private void enviarObjetoPersistenciaTransferido() throws IOException, ClassNotFoundException {

        flujoSalidaObjeto.writeObject(marketPlace);

        System.out.println("objeto enviado");
        flujoSalidaObjeto.close();
    }

    public void verificarLogin() throws IOException {
        flujoSalidaComunicacion.writeInt(3);
       //flujoSalidaComunicacion.close();
    }
    public void enviarDatosUsuario(String nombreUsuario, String contrasenia) throws IOException {
        Usuario usuario = new Usuario(nombreUsuario,contrasenia);
        flujoSalidaObjeto.writeObject(usuario);
        flujoSalidaObjeto.close();
    }
    public void iniciarConexionLogin(String nombreUsuario, String contrasenia) throws Exception {

        crearConexion();
        verificarLogin();
        enviarDatosUsuario(nombreUsuario, contrasenia);

         respuestaAdmin =  flujoEntradaComunicacion.readBoolean();
         if(!respuestaAdmin){
             respuestaVendedor =  flujoEntradaComunicacion.readBoolean();
         }

        flujoEntradaComunicacion.close();

    }

    public void opcionLeerRespuestLogin() throws IOException {
        flujoSalidaComunicacion.writeInt(4);
        flujoSalidaComunicacion.close();
    }

    public boolean leerLogin() throws Exception {
        //crearConexion();
        //opcionLeerRespuestLogin();

        return flujoEntradaComunicacion.readBoolean();
    }



    public Comentario crearComentario(Vendedor vendedorEnviado, Producto productoComentado, String nombre, String identificacion, String comentario) {
        try {
            Comentario c1 = this.getMarketPlace().crearComentario(vendedorEnviado, productoComentado, nombre, identificacion, comentario);

            return c1;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }




}
