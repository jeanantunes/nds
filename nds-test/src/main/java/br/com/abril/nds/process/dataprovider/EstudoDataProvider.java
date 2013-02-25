package br.com.abril.nds.process.dataprovider;

import org.testng.annotations.DataProvider;

import br.com.abril.nds.model.Estudo;

public class EstudoDataProvider {

    private static EstudoDataProvider instance;
    private static Estudo currentEstudo;

    private EstudoDataProvider() {
    }

    public static EstudoDataProvider getInstance() {
	if (instance == null) {
	    instance = new EstudoDataProvider();
	}

	return instance;
    }

    public void setEstudo(Estudo estudo) {
	currentEstudo = estudo;
    }

    @DataProvider(name = "getCurrentEstudo")
    public static Estudo[][] getCurrentEstudo() {
	return new Estudo[][] { { currentEstudo } };
    }

}
