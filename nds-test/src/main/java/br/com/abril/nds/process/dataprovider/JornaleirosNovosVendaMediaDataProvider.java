package br.com.abril.nds.process.dataprovider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;

public abstract class JornaleirosNovosVendaMediaDataProvider {

    @DataProvider(name = "getCotaComEquivalenteList")
    public static Iterator<Cota[]> getCotaComEquivalenteList() {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	Estudo estudo = new Estudo();
	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();
	estudo.setCotas(listCota);

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    Cota cota = itCota.next();
	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    cota = new CotaDAO().getCotaEquivalenteByCota(cota);

	    if (cota.isNova()) {
		listCotaReturn.add(new Cota[] { cota });
	    }

	}

	return listCotaReturn.iterator();
    }

}
