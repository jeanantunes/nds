package br.com.abril.nds.process.dataprovider;

import java.math.BigDecimal;
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

public abstract class CorrecaoTendenciaDataProvider {

    @DataProvider(name = "getCotaParaPercentualVendaNaoIgualUmNaoMaiorIgualZeroVirgulaNoveList")
    public static Iterator<Object[]> getCotaParaPercentualVendaNaoIgualUmNaoMaiorIgualZeroVirgulaNoveList() {

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	List<Object[]> listCotaReturn = new ArrayList<Object[]>();

	int iCota = 0;
	while (iCota < listCota.size()) {

	    Cota cota = listCota.get(iCota);

	    List<ProdutoEdicao> listProdutoEdicao = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);
	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, listProdutoEdicao);
	    List<ProdutoEdicao> listProdutoEdicaoReturn = new ArrayList<ProdutoEdicao>();

	    BigDecimal totalReparte = BigDecimal.ZERO;
	    BigDecimal totalVenda = BigDecimal.ZERO;

	    int iEstoqueProdutoCota = 0;
	    while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEstoqueProdutoCota);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));

		if (produtoEdicao.getReparte().compareTo(new BigDecimal(8)) == -1) {
		    if (produtoEdicao.getVenda().compareTo(produtoEdicao.getReparte().subtract(BigDecimal.ONE)) == 0) {
			totalReparte = totalReparte.add(produtoEdicao.getReparte());
			totalVenda = totalVenda.add(produtoEdicao.getVenda());
			listProdutoEdicaoReturn.add(produtoEdicao);
		    }
		}

		iEstoqueProdutoCota++;

	    }

	    if (listProdutoEdicaoReturn != null && !listProdutoEdicaoReturn.isEmpty()) {
		cota.setEdicoesRecebidas(listProdutoEdicaoReturn);
		listCotaReturn.add(new Object[] { cota, totalReparte, totalVenda });
	    }

	    iCota++;
	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getEstoqueProdutoCotaParaPercentualVendaIgualUmList")
    public static Iterator<Object[]> getEstoqueProdutoCotaParaPercentualVendaIgualUmList() {

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	List<Object[]> listCotaReturn = new ArrayList<Object[]>();

	int iCota = 0;
	while (iCota < listCota.size()) {

	    Cota cota = listCota.get(iCota);

	    List<ProdutoEdicao> listProdutoEdicao = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);
	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, listProdutoEdicao);
	    List<ProdutoEdicao> listProdutoEdicaoReturn = new ArrayList<ProdutoEdicao>();

	    BigDecimal totalReparte = BigDecimal.ZERO;
	    BigDecimal totalVenda = BigDecimal.ZERO;

	    int iEstoqueProdutoCota = 0;
	    while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEstoqueProdutoCota);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));

		if (estoqueProdutoCota.getQuantidadeDevolvida().compareTo(BigDecimal.ZERO) <= 0) {
		    if (produtoEdicao.getVenda().compareTo(produtoEdicao.getReparte()) == 0) {
			totalReparte = totalReparte.add(produtoEdicao.getReparte());
			totalVenda = totalVenda.add(produtoEdicao.getVenda());
			listProdutoEdicaoReturn.add(produtoEdicao);
		    }
		}

		iEstoqueProdutoCota++;

	    }

	    if (listProdutoEdicaoReturn != null && !listProdutoEdicaoReturn.isEmpty()) {
		cota.setEdicoesRecebidas(listProdutoEdicaoReturn);
		listCotaReturn.add(new Object[] { cota, totalReparte, totalVenda });
	    }

	    iCota++;
	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getEstoqueProdutoCotaParaPercentualVendaMaiorIgualZeroVirgulaNoveList")
    public static Iterator<Object[]> getEstoqueProdutoCotaParaPercentualVendaMaiorIgualZeroVirgulaNoveList() {

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	List<Object[]> listCotaReturn = new ArrayList<Object[]>();

	int iCota = 0;
	while (iCota < listCota.size()) {

	    Cota cota = listCota.get(iCota);

	    List<ProdutoEdicao> listProdutoEdicao = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);
	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, listProdutoEdicao);
	    List<ProdutoEdicao> listProdutoEdicaoReturn = new ArrayList<ProdutoEdicao>();

	    BigDecimal totalReparte = BigDecimal.ZERO;
	    BigDecimal totalVenda = BigDecimal.ZERO;

	    int iEstoqueProdutoCota = 0;
	    while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEstoqueProdutoCota);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());

		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));

		if (produtoEdicao.getVenda().compareTo(new BigDecimal(9).divide(BigDecimal.ONE, 1, BigDecimal.ROUND_FLOOR)) >= 0
			&& produtoEdicao.getVenda().add(BigDecimal.ONE).compareTo(produtoEdicao.getReparte()) == 0) {
		    totalReparte = totalReparte.add(produtoEdicao.getReparte());
		    totalVenda = totalVenda.add(produtoEdicao.getVenda());
		    listProdutoEdicaoReturn.add(produtoEdicao);
		}

		iEstoqueProdutoCota++;

	    }

	    if (totalVenda.add(BigDecimal.ONE).compareTo(totalReparte) == 0) {
		if (listProdutoEdicaoReturn != null && !listProdutoEdicaoReturn.isEmpty()) {
		    cota.setEdicoesRecebidas(listProdutoEdicaoReturn);
		    listCotaReturn.add(new Object[] { cota, totalReparte, totalVenda });
		}
	    }

	    iCota++;
	}

	return listCotaReturn.iterator();
    }
}
