package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;
import java.math.BigInteger;
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

public abstract class CorrecaoVendasDataProvider {

    @DataProvider(name = "getCotaList")
    public static Iterator<Cota[]> getCotaList() {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	int iCota = 0;
	while (iCota < listCota.size()) {

	    Cota cota = listCota.get(iCota);

	    // TODO As edições base já deveriam vir preenchidas
	    // FIXME Retirar a chamada para ProdutoEdicaoDAO
	    cota.setEdicoesBase(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));
	    listCotaReturn.add(new Cota[] { cota });

	    iCota++;
	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getEstoqueProdutoCotaList")
    public static Iterator<EstoqueProdutoCota[]> getEstoqueProdutoCotaList() {

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	List<EstoqueProdutoCota[]> listEstoqueProdutoCotas = new ArrayList<EstoqueProdutoCota[]>();

	int iCota = 0;
	while (iCota < listCota.size()) {

	    Cota cota = listCota.get(iCota);

	    cota.setEdicoesBase(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO()
	    .getByCotaIdProdutoEdicaoId(cota, cota.getEdicoesBase());

	    int iEstoqueProdutoCota = 0;
	    while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota
			.get(iEstoqueProdutoCota);

		if (estoqueProdutoCota.getProdutoEdicao().getNumeroEdicao()
			.compareTo(new Long(1)) == 0
			|| !estoqueProdutoCota.getProdutoEdicao().isColecao()) {

		    listEstoqueProdutoCotas
		    .add(new EstoqueProdutoCota[] { estoqueProdutoCota });
		}

		iEstoqueProdutoCota++;

	    }

	    iCota++;
	}

	return listEstoqueProdutoCotas.iterator();
    }
    
    @DataProvider(name = "getEstoqueProdutoCotaParaPercentualVendaUmList")
    public static Iterator<EstoqueProdutoCota[]> getEstoqueProdutoCotaParaPercentualVendaUmList() {

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	List<EstoqueProdutoCota[]> listEstoqueProdutoCotas = new ArrayList<EstoqueProdutoCota[]>();

	int iCota = 0;
	while (iCota < listCota.size()) {

	    Cota cota = listCota.get(iCota);

	    cota.setEdicoesBase(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO()
	    .getByCotaIdProdutoEdicaoId(cota, cota.getEdicoesBase());

	    int iEstoqueProdutoCota = 0;
	    while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota
			.get(iEstoqueProdutoCota);
		
		//Esse ponto faz o valor do percentual ser 1. Pois, é a divisão pelo mesmo valor!
		estoqueProdutoCota.setQuantidadeDevolvida(BigDecimal.ZERO);
		
		if (estoqueProdutoCota.getProdutoEdicao().getNumeroEdicao()
			.compareTo(new Long(1)) == 0
			|| !estoqueProdutoCota.getProdutoEdicao().isColecao()) {

		    listEstoqueProdutoCotas
		    .add(new EstoqueProdutoCota[] { estoqueProdutoCota });
		}

		iEstoqueProdutoCota++;

	    }

	    iCota++;
	}

	return listEstoqueProdutoCotas.iterator();
    }

    @DataProvider(name = "getCotaTotalReparteVendaList")
    public static Iterator<Object[]> getCotaTotalReparteVendaList() {

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	List<Object[]> listEstoqueProdutoCotas = new ArrayList<Object[]>();

	int iCota = 0;
	while (iCota < listCota.size()) {

	    Cota cota = listCota.get(iCota);

	    cota.setEdicoesBase(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO()
	    .getByCotaIdProdutoEdicaoId(cota, cota.getEdicoesBase());

	    BigDecimal totalReparte = BigDecimal.ZERO;
	    BigDecimal totalVenda = BigDecimal.ZERO;

	    boolean addCota = false;

	    if (cota.getEdicoesBase() != null
		    && cota.getEdicoesBase().size() > 1) {

		int iEstoqueProdutoCota = 0;
		while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		    EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota
			    .get(iEstoqueProdutoCota);

		    if (estoqueProdutoCota.getProdutoEdicao().getNumeroEdicao()
			    .compareTo(new Long(1)) == 0
			    || !estoqueProdutoCota.getProdutoEdicao()
			    .isColecao()) {

			addCota = true;

			BigInteger quantidadeRecebida = estoqueProdutoCota
				.getQuantidadeRecebida().toBigInteger();
			BigInteger quantidadeDevolvida = estoqueProdutoCota
				.getQuantidadeDevolvida().toBigInteger();

			totalReparte = totalReparte.add(new BigDecimal(
				quantidadeRecebida));

			BigInteger venda = quantidadeRecebida
				.subtract(quantidadeDevolvida);

			totalVenda = totalVenda.add(new BigDecimal(venda));

		    }

		    iEstoqueProdutoCota++;
		}
	    }

	    if (addCota) {
		listEstoqueProdutoCotas.add(new Object[] { cota, totalReparte,
			totalVenda });
	    }

	    iCota++;
	}

	return listEstoqueProdutoCotas.iterator();
    }

    @DataProvider(name = "getCotaEdicaoBaseUnicaPublicacaoList")
    public static Iterator<Cota[]> getCotaEdicaoBaseUnicaPublicacaoList() {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	Estudo estudo = new Estudo();
	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();
	estudo.setCotas(listCota);

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    List<EstoqueProdutoCota> listEstoqueProdutoEdicaoUnicaPublicacao = new ArrayList<EstoqueProdutoCota>();

	    Cota cota = itCota.next();

	    // TODO As edições base já deveriam vir preenchidas
	    // FIXME Retirar a chamada para ProdutoEdicaoDAO
	    cota.setEdicoesBase(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO()
	    .getByCotaIdProdutoEdicaoId(cota, cota.getEdicoesBase());

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota
			.get(iEdicaoBase);

		if (estoqueProdutoCota.getProdutoEdicao().getNome()
			.equalsIgnoreCase("MALU.")) {

		    listEstoqueProdutoEdicaoUnicaPublicacao
		    .add(estoqueProdutoCota);
		}

		iEdicaoBase++;
	    }

	    cota.setEstoqueProdutoCotas(listEstoqueProdutoEdicaoUnicaPublicacao);

	    listCotaReturn.add(new Cota[] { cota });
	}

	return listCotaReturn.iterator();
    }
}
