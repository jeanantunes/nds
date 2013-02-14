package br.com.abril.nds.process.correcaovendas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.EstoqueProdutoCotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;

public abstract class CorrecaoVendasDataProvider {

    @SuppressWarnings("unchecked")
    @DataProvider(name = "getCotaList")
    public static Iterator<List<Cota>[]> getCotaList() {

	List<List<Cota>[]> listCotaReturn = new ArrayList<List<Cota>[]>();

	List<Cota> listCota = new CotaDAO().getCotas();

	int iCota = 0;
	while (iCota < listCota.size()) {

	    Cota cota = listCota.get(iCota);

	    // TODO As edições base já deveriam vir preenchidas
	    // FIXME Retirar a chamada para ProdutoEdicaoDAO
	    cota.setEdicoesBase(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    iCota++;
	}

	listCotaReturn.add(new List[] { listCota });

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getEstoqueProdutoCotaList")
    public static Iterator<EstoqueProdutoCota[]> getEstoqueProdutoCotaList() {

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

    @DataProvider(name = "getEdicaoBaseFechadaList")
    public static Iterator<Object[]> getEdicaoBaseFechadaList() {

	List<Object[]> listProdutoEdicaoFechadaReturn = new ArrayList<Object[]>();

	Estudo estudo = new Estudo();
	List<Cota> listCota = new CotaDAO().getCotas();
	estudo.setCotas(listCota);

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    List<ProdutoEdicao> listProdutoEdicaoFechada = new ArrayList<ProdutoEdicao>();

	    Cota cota = itCota.next();

	    // TODO As edições base já deveriam vir preenchidas
	    // FIXME Retirar a chamada para ProdutoEdicaoDAO
	    cota.setEdicoesBase(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    List<ProdutoEdicao> listEdicaoBase = cota.getEdicoesBase();

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < listEdicaoBase.size()) {

		ProdutoEdicao produtoEdicaoBase = listEdicaoBase
			.get(iEdicaoBase);
		if (!produtoEdicaoBase.isEdicaoAberta()) {
		    listProdutoEdicaoFechada.add(produtoEdicaoBase);
		}

		iEdicaoBase++;
	    }

	    listProdutoEdicaoFechadaReturn.add(new Object[] { cota,
		    listProdutoEdicaoFechada });

	}

	return listProdutoEdicaoFechadaReturn.iterator();
    }

}
