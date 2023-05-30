package co.edu.uqvirtual.markerplace.crud;

import co.edu.uqvirtual.markerplace.controllers.ModelFactoryController;
import co.edu.uqvirtual.markerplace.modelo.Administrador;
import co.edu.uqvirtual.markerplace.modelo.MarketPlace;

import java.io.IOException;

public class CrudAdminViewController {
    ModelFactoryController modelFactoryController;
    MarketPlace marketPlace;


    public CrudAdminViewController(ModelFactoryController modelFactoryController) {
        this.modelFactoryController = modelFactoryController;
        marketPlace = modelFactoryController.getMarketPlace();
    }


    public void cambiarEstadoLoginAdmin(){

        if(marketPlace.getAdmin().isLogin()){
            modelFactoryController.getMarketPlace().getAdmin().setLogin(false);
        }else{
            modelFactoryController.getMarketPlace().getAdmin().setLogin(true);
        }


    }
    public Administrador obtenerAdmin(){
        return modelFactoryController.getMarketPlace().getAdmin();
    }
    public void verificarLoginAdmin(String nombreUsuario, String contrasenia) throws Exception {
        modelFactoryController.iniciarConexionLogin(nombreUsuario, contrasenia);
    }
    public MarketPlace getMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(MarketPlace marketPlace) {
        this.marketPlace = marketPlace;
    }
}
