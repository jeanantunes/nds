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
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;

public abstract class CorrecaoVendasDataProvider {

    @DataProvider(name = "getCotaUnicaPublicacaoMenosDeQuatroEdicoesList")
    public static Iterator<Cota[]> getCotaUnicaPublicacaoMenosDeQuatroEdicoesList() {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	Estudo estudo = new Estudo();
	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();
	estudo.setCotas(listCota);

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();

	    Cota cota = itCota.next();

	    List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();

		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));

		// Produto MALU. -> 12913
		if (produtoEdicao.getIdProduto().equals(new Long(12913))) {
		    if (edicoesRecebidas.size() < 3) {
			edicoesRecebidas.add(produtoEdicao);
		    }
		}

		iEdicaoBase++;
	    }

	    if (edicoesRecebidas != null && !edicoesRecebidas.isEmpty()) {
		cota.setEdicoesRecebidas(edicoesRecebidas);
		listCotaReturn.add(new Cota[] { cota });
	    }

	    listCotaReturn.add(new Cota[] { cota });

	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getCotaUnicaPublicacaoQuatroEdicoesSemEdicoesFechadaList")
    public static Iterator<Cota[]> getCotaUnicaPublicacaoQuatroEdicoesSemEdicoesFechadaList() {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	Estudo estudo = new Estudo();
	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();
	estudo.setCotas(listCota);

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();

	    Cota cota = itCota.next();

	    if (cota.getId().longValue() == 29) {
		System.out.println();
	    }

	    // TODO As edições base já deveriam vir preenchidas
	    // FIXME Retirar a chamada para ProdutoEdicaoDAO
	    List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();

		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));
		produtoEdicao.setEdicaoAberta(true);

		// Produto MALU. -> 12913
		if (produtoEdicao.getIdProduto().equals(new Long(12913))) {
		    if (edicoesRecebidas.size() < 4) {
			edicoesRecebidas.add(produtoEdicao);
		    }
		}

		iEdicaoBase++;
	    }

	    if (edicoesRecebidas != null && !edicoesRecebidas.isEmpty()) {
		cota.setEdicoesRecebidas(edicoesRecebidas);
		listCotaReturn.add(new Cota[] { cota });
	    }
	}

	return listCotaReturn.iterator();
    }
}
