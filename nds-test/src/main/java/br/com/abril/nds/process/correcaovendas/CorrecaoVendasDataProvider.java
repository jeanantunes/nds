package br.com.abril.nds.process.correcaovendas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.EstoqueProdutoCotaDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;

public abstract class CorrecaoVendasDataProvider {

    @SuppressWarnings("unchecked")
    @DataProvider(name = "getCotaList")
    public static Iterator<List<Cota>[]> getCotaList() {

	List<List<Cota>[]> listCotaReturn = new ArrayList<List<Cota>[]>();

	List<Cota> listCota = new CotaDAO().getCotas();

	listCotaReturn.add(new List[] { listCota });

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getEstoqueProdutoCota")
    private static Iterator<EstoqueProdutoCota[]> getEstoqueProdutoCota() {

	List<Cota> listCota = new CotaDAO().getCotas();

	List<EstoqueProdutoCota[]> listEstoqueProdutoCotas = new ArrayList<EstoqueProdutoCota[]>();

	int iCota = 0;
	while (iCota < listCota.size()) {

	    Cota cota = listCota.get(iCota);

	    List<EstoqueProdutoCota> listEstoqueCota = new EstoqueProdutoCotaDAO()
		    .getByCotaId(cota.getId());

	    int iEst = 0;
	    while (iEst < listEstoqueCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueCota
			.get(iEst);
		listEstoqueProdutoCotas
			.add(new EstoqueProdutoCota[] { estoqueProdutoCota });
		iEst++;
	    }

	    iCota++;
	}

	return listEstoqueProdutoCotas.iterator();
    }

}
