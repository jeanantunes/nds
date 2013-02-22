package br.com.abril.nds.process.dataprovider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.EstoqueProdutoCotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;

public abstract class VendasCrescenteDataProvider {

    @DataProvider(name = "getCotaParaUnicaPublicacaoMenosDeQuatroEdicoesList")
    public static Iterator<Cota[]> getCotaParaUnicaPublicacaoMenosDeQuatroEdicoesList() {

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

	    Map<Long, List<Integer>> mapIdRepeticao = new HashMap<Long, List<Integer>>();

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();

		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));

		if (mapIdRepeticao.containsKey(produtoEdicao.getIdProduto())) {
		    List<Integer> listIndex = mapIdRepeticao.get(produtoEdicao.getIdProduto());
		    listIndex.add(iEdicaoBase);
		    mapIdRepeticao.put(produtoEdicao.getIdProduto(), listIndex);
		} else {
		    List<Integer> listIndex = new ArrayList<Integer>();
		    listIndex.add(iEdicaoBase);
		    mapIdRepeticao.put(produtoEdicao.getIdProduto(), listIndex);
		}

		iEdicaoBase++;
	    }

	    if (mapIdRepeticao.size() == 1) {
		Iterator<Long> itMap = mapIdRepeticao.keySet().iterator();
		while (itMap.hasNext()) {
		    List<Integer> listIndex = mapIdRepeticao.get(itMap.next());
		    if (listIndex.size() < 3) {
			int iIndex = 0;
			while (iIndex < listIndex.size()) {
			    edicoesRecebidas.add(listEstoqueProdutoCota.get(listIndex.get(iIndex)).getProdutoEdicao());
			    iIndex++;
			}
		    }
		}
	    }

	    if (edicoesRecebidas != null && !edicoesRecebidas.isEmpty()) {
		cota.setEdicoesRecebidas(edicoesRecebidas);
		listCotaReturn.add(new Cota[] { cota });
	    }

	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getCotaParaUnicaPublicacaoQuatroEdicoesSemEdicoesFechadaList")
    public static Iterator<Cota[]> getCotaParaUnicaPublicacaoQuatroEdicoesSemEdicoesFechadaList() {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	Estudo estudo = new Estudo();
	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();
	estudo.setCotas(listCota);

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();

	    Cota cota = itCota.next();

	    // TODO As edições base já deveriam vir preenchidas
	    // FIXME Retirar a chamada para ProdutoEdicaoDAO
	    List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);

	    Map<Long, List<Integer>> mapIdRepeticao = new HashMap<Long, List<Integer>>();

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();

		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));
		//TODO Essa propriedade deveria vir setada
		//FIXME Verificar
		produtoEdicao.setEdicaoAberta(true);

		if (mapIdRepeticao.containsKey(produtoEdicao.getIdProduto())) {
		    List<Integer> listIndex = mapIdRepeticao.get(produtoEdicao.getIdProduto());
		    listIndex.add(iEdicaoBase);
		    mapIdRepeticao.put(produtoEdicao.getIdProduto(), listIndex);
		} else {
		    List<Integer> listIndex = new ArrayList<Integer>();
		    listIndex.add(iEdicaoBase);
		    mapIdRepeticao.put(produtoEdicao.getIdProduto(), listIndex);
		}

		iEdicaoBase++;
	    }

	    if (mapIdRepeticao.size() == 1) {
		Iterator<Long> itMap = mapIdRepeticao.keySet().iterator();
		while (itMap.hasNext()) {
		    List<Integer> listIndex = mapIdRepeticao.get(itMap.next());
		    if (listIndex.size() >= 4) {
			int iIndex = 0;
			while (iIndex < listIndex.size()) {
			    edicoesRecebidas.add(listEstoqueProdutoCota.get(listIndex.get(iIndex)).getProdutoEdicao());
			    iIndex++;
			}
		    }
		}
	    }

	    if (edicoesRecebidas != null && !edicoesRecebidas.isEmpty()) {
		cota.setEdicoesRecebidas(edicoesRecebidas);
		listCotaReturn.add(new Cota[] { cota });
	    }
	}

	return listCotaReturn.iterator();
    }

}
