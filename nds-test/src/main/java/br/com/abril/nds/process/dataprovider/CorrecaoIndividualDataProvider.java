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

public abstract class CorrecaoIndividualDataProvider {

    @DataProvider(name = "getEstoqueProdutoCotaParaPercentualVendaNaoIgualUmNaoIgualMaiorZeroVirgualNoveList")
    public static Iterator<EstoqueProdutoCota[]> getEstoqueProdutoCotaParaPercentualVendaNaoIgualUmNaoIgualMaiorZeroVirgualNoveList() {

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	List<EstoqueProdutoCota[]> listEstoqueProdutoCotas = new ArrayList<EstoqueProdutoCota[]>();

	int iCota = 0;
	while (iCota < listCota.size()) {

	    Cota cota = listCota.get(iCota);

	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, cota.getEdicoesRecebidas());

	    int iEstoqueProdutoCota = 0;
	    while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEstoqueProdutoCota);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));

		if (produtoEdicao.getReparte().compareTo(new BigDecimal(8)) == -1) {
		    if (produtoEdicao.getVenda().compareTo(produtoEdicao.getReparte().subtract(BigDecimal.ONE)) == 0) {
			listEstoqueProdutoCotas.add(new EstoqueProdutoCota[] { estoqueProdutoCota });
		    }
		}

		iEstoqueProdutoCota++;

	    }

	    iCota++;
	}

	return listEstoqueProdutoCotas.iterator();
    }

    @DataProvider(name = "getEstoqueProdutoCotaParaPercentualVendaIgualUmList")
    public static Iterator<EstoqueProdutoCota[]> getEstoqueProdutoCotaParaPercentualVendaIgualUmList() {

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	List<EstoqueProdutoCota[]> listEstoqueProdutoCotas = new ArrayList<EstoqueProdutoCota[]>();

	int iCota = 0;
	while (iCota < 10) {

	    Cota cota = listCota.get(iCota);

	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, cota.getEdicoesRecebidas());

	    int iEstoqueProdutoCota = 0;
	    while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEstoqueProdutoCota);
		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));

		if (estoqueProdutoCota.getQuantidadeDevolvida().compareTo(BigDecimal.ZERO) <= 0) {
		    if (produtoEdicao.getVenda().compareTo(produtoEdicao.getReparte()) == 0) {
			listEstoqueProdutoCotas.add(new EstoqueProdutoCota[] { estoqueProdutoCota });
		    }
		}

		iEstoqueProdutoCota++;

	    }

	    iCota++;
	}

	return listEstoqueProdutoCotas.iterator();
    }

    @DataProvider(name = "getEstoqueProdutoCotaParaPercentualVendaMaiorIgualZeroVirgulaNoveList")
    public static Iterator<EstoqueProdutoCota[]> getEstoqueProdutoCotaParaPercentualVendaMaiorIgualZeroVirgulaNoveList() {

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	List<EstoqueProdutoCota[]> listEstoqueProdutoCotas = new ArrayList<EstoqueProdutoCota[]>();

	int iCota = 0;
	while (iCota < listCota.size()) {

	    Cota cota = listCota.get(iCota);

	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, cota.getEdicoesRecebidas());

	    int iEstoqueProdutoCota = 0;
	    while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEstoqueProdutoCota);
		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));

		if (produtoEdicao.getVenda().compareTo(new BigDecimal(9).divide(BigDecimal.ONE, 1, BigDecimal.ROUND_FLOOR)) >= 0
			&& produtoEdicao.getVenda().add(BigDecimal.ONE).compareTo(produtoEdicao.getReparte()) == 0) {
		    listEstoqueProdutoCotas.add(new EstoqueProdutoCota[] { estoqueProdutoCota });
		}

		iEstoqueProdutoCota++;

	    }

	    iCota++;
	}

	return listEstoqueProdutoCotas.iterator();
    }
}
