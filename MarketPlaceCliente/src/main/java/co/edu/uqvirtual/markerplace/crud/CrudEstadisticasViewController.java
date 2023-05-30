package co.edu.uqvirtual.markerplace.crud;

import co.edu.uqvirtual.markerplace.controllers.ModelFactoryController;
import co.edu.uqvirtual.markerplace.modelo.MarketPlace;

public class CrudEstadisticasViewController {
    ModelFactoryController modelFactoryController;
    MarketPlace marketPlace;

    public CrudEstadisticasViewController(ModelFactoryController modelFactoryController) {
        this.modelFactoryController = modelFactoryController;
        this.marketPlace = modelFactoryController.getMarketPlace();
    }


    public void crearArchivoEstadisticas(String rutaElegida, String contenido){
        modelFactoryController.crearArchivoEstadisticas(rutaElegida, contenido);
    }
}
