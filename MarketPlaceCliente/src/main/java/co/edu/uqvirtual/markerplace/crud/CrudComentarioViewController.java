package co.edu.uqvirtual.markerplace.crud;

import co.edu.uqvirtual.markerplace.controllers.ModelFactoryController;
import co.edu.uqvirtual.markerplace.modelo.MarketPlace;

public class CrudComentarioViewController {
    ModelFactoryController modelFactoryController;
    MarketPlace marketPlace;

    public CrudComentarioViewController(ModelFactoryController modelFactoryController) {
        this.modelFactoryController = modelFactoryController;
        marketPlace = modelFactoryController.getMarketPlace();
    }

}
