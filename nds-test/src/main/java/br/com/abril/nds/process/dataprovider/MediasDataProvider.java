package br.com.abril.nds.process.dataprovider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.EstoqueProdutoCotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.correcaovendas.CorrecaoIndividual;

public abstract class MediasDataProvider {

    @DataProvider(name = "getCotaQuantidadeEdicoesMenorTresList")
    public static Iterator<Cota[]> getCotaQuantidadeEdicoesMenorTresList() throws Exception {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();

	    Cota cota = itCota.next();

	    List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);
	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);

	    if (listEstoqueProdutoCota != null && !listEstoqueProdutoCota.isEmpty() && listEstoqueProdutoCota.size() < 3) {
		int iEdicaoBase = 0;
		while (iEdicaoBase < listEstoqueProdutoCota.size()) {

		    EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);
		    ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
		    produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		    produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));
		    produtoEdicao.setPeso(new Integer(1));

		    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(produtoEdicao);
		    correcaoIndividual.executar();

		    edicoesRecebidas.add(produtoEdicao);

		    iEdicaoBase++;
		}

		cota.setEdicoesRecebidas(edicoesRecebidas);
		listCotaReturn.add(new Cota[] { cota });
	    }
	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getCotaQuantidadeEdicoesMaiorIgualTresList")
    public static Iterator<Cota[]> getCotaQuantidadeEdicoesMaiorIgualTresList() throws Exception {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();

	    Cota cota = itCota.next();

	    List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);
	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);

	    if (listEstoqueProdutoCota != null && !listEstoqueProdutoCota.isEmpty() && listEstoqueProdutoCota.size() >= 3) {
		int iEdicaoBase = 0;
		while (iEdicaoBase < listEstoqueProdutoCota.size()) {

		    EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);
		    ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
		    produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		    produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));
		    produtoEdicao.setPeso(new Integer(1));

		    CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(produtoEdicao);
		    correcaoIndividual.executar();

		    edicoesRecebidas.add(produtoEdicao);

		    iEdicaoBase++;
		}

		cota.setEdicoesRecebidas(edicoesRecebidas);
		listCotaReturn.add(new Cota[] { cota });
	    }
	}

	return listCotaReturn.iterator();
    }

}
