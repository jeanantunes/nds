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
import br.com.abril.nds.model.ProdutoEdicao;

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
	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO()
		    .getEdicaoRecebidas(cota));
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

	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO()
		    .getEdicaoRecebidas(cota));

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO()
		    .getByCotaIdProdutoEdicaoId(cota,
			    cota.getEdicoesRecebidas());

	    int iEstoqueProdutoCota = 0;
	    while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota
			.get(iEstoqueProdutoCota);

		listEstoqueProdutoCotas
			.add(new EstoqueProdutoCota[] { estoqueProdutoCota });

		iEstoqueProdutoCota++;

	    }

	    iCota++;
	}

	return listEstoqueProdutoCotas.iterator();
    }

    @DataProvider(name = "getEstoqueProdutoCotaParaIndiceCorrecaoUmPontoDoisList")
    public static Iterator<EstoqueProdutoCota[]> getEstoqueProdutoCotaParaIndiceCorrecaoUmPontoDoisList() {

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	List<EstoqueProdutoCota[]> listEstoqueProdutoCotas = new ArrayList<EstoqueProdutoCota[]>();

	int iCota = 0;
	while (iCota < 10) {

	    Cota cota = listCota.get(iCota);

	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO()
		    .getEdicaoRecebidas(cota));

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO()
		    .getByCotaIdProdutoEdicaoId(cota,
			    cota.getEdicoesRecebidas());

	    int iEstoqueProdutoCota = 0;
	    while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota
			.get(iEstoqueProdutoCota);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota
			.getProdutoEdicao();

		produtoEdicao.setReparte(estoqueProdutoCota
			.getQuantidadeRecebida());

		// Esse trecho faz o valor do percentual ser 1.2. Pois, é a divisão pelo mesmo valor!
		produtoEdicao.setVenda(estoqueProdutoCota
			.getQuantidadeRecebida());

		listEstoqueProdutoCotas
			.add(new EstoqueProdutoCota[] { estoqueProdutoCota });

		iEstoqueProdutoCota++;

	    }

	    iCota++;
	}

	return listEstoqueProdutoCotas.iterator();
    }

    @DataProvider(name = "getEstoqueProdutoCotaParaIndiceCorrecaoUmPontoUmList")
    public static Iterator<EstoqueProdutoCota[]> getEstoqueProdutoCotaParaIndiceCorrecaoUmPontoUmList() {

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	List<EstoqueProdutoCota[]> listEstoqueProdutoCotas = new ArrayList<EstoqueProdutoCota[]>();

	int iCota = 0;
	while (iCota < 10) {

	    Cota cota = listCota.get(iCota);

	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO()
		    .getEdicaoRecebidas(cota));

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO()
		    .getByCotaIdProdutoEdicaoId(cota,
			    cota.getEdicoesRecebidas());

	    int iEstoqueProdutoCota = 0;
	    while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota
			.get(iEstoqueProdutoCota);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota
			.getProdutoEdicao();

		produtoEdicao.setReparte(estoqueProdutoCota
			.getQuantidadeRecebida());

		// Esse trecho faz o valor do percentual ser 1.1.
		if(produtoEdicao.getReparte().compareTo(BigDecimal.TEN) >= 0) {
		    produtoEdicao.setVenda(produtoEdicao.getReparte().subtract(
			    BigDecimal.ONE));
		    
		    listEstoqueProdutoCotas
		    .add(new EstoqueProdutoCota[] { estoqueProdutoCota });
		}

		iEstoqueProdutoCota++;

	    }

	    iCota++;
	}

	return listEstoqueProdutoCotas.iterator();
    }

    @DataProvider(name = "getEstoqueProdutoCotaParaIndiceCorrecaoUmList")
    public static Iterator<EstoqueProdutoCota[]> getEstoqueProdutoCotaParaIndiceCorrecaoUmList() {

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	List<EstoqueProdutoCota[]> listEstoqueProdutoCotas = new ArrayList<EstoqueProdutoCota[]>();

	int iCota = 0;
	while (iCota < 10) {

	    Cota cota = listCota.get(iCota);

	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO()
		    .getEdicaoRecebidas(cota));

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO()
		    .getByCotaIdProdutoEdicaoId(cota,
			    cota.getEdicoesRecebidas());

	    int iEstoqueProdutoCota = 0;
	    while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota
			.get(iEstoqueProdutoCota);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota
			.getProdutoEdicao();

		produtoEdicao.setReparte(estoqueProdutoCota
			.getQuantidadeRecebida());

		// Esse trecho faz o valor do percentual ser 1.
		// produtoEdicao.setVenda();

		listEstoqueProdutoCotas
			.add(new EstoqueProdutoCota[] { estoqueProdutoCota });

		iEstoqueProdutoCota++;

	    }

	    iCota++;
	}

	return listEstoqueProdutoCotas.iterator();
    }

    @DataProvider(name = "getCotaProdutoEdicaoPrimeiraEdicaoColecaoList")
    public static Iterator<Object[]> getCotaProdutoEdicaoPrimeiraEdicaoColecaoList() {

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	List<Object[]> listProdutoEdicaoCota = new ArrayList<Object[]>();

	int iCota = 0;
	while (iCota < listCota.size()) {

	    Cota cota = listCota.get(iCota);

	    cota.setEdicoesRecebidas(new ArrayList<ProdutoEdicao>());

	    List<ProdutoEdicao> edicoesRecebidas = new ProdutoEdicaoDAO()
		    .getEdicaoRecebidas(cota);

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO()
		    .getByCotaIdProdutoEdicaoId(cota, edicoesRecebidas);

	    BigDecimal totalReparte = BigDecimal.ZERO;
	    BigDecimal totalVenda = BigDecimal.ZERO;

	    if (edicoesRecebidas != null && edicoesRecebidas.size() > 1) {

		int iEstoqueProdutoCota = 0;
		while (iEstoqueProdutoCota < listEstoqueProdutoCota.size()) {

		    EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota
			    .get(iEstoqueProdutoCota);

		    ProdutoEdicao produtoEdicao = estoqueProdutoCota
			    .getProdutoEdicao();

		    if (produtoEdicao.getNumeroEdicao().compareTo(new Long(1)) == 0
			    || !produtoEdicao.isColecao()) {

			BigInteger quantidadeRecebida = estoqueProdutoCota
				.getQuantidadeRecebida().toBigInteger();
			BigInteger quantidadeDevolvida = estoqueProdutoCota
				.getQuantidadeDevolvida().toBigInteger();

			totalReparte = totalReparte.add(new BigDecimal(
				quantidadeRecebida));

			BigInteger venda = quantidadeRecebida
				.subtract(quantidadeDevolvida);

			totalVenda = totalVenda.add(new BigDecimal(venda));

			produtoEdicao.setReparte(estoqueProdutoCota
				.getQuantidadeRecebida());

			produtoEdicao.setVenda(estoqueProdutoCota
				.getQuantidadeRecebida().subtract(
					estoqueProdutoCota
						.getQuantidadeDevolvida()));

			cota.getEdicoesRecebidas().add(produtoEdicao);

		    }

		    iEstoqueProdutoCota++;
		}
	    }

	    if (!cota.getEdicoesRecebidas().isEmpty()) {

		listProdutoEdicaoCota.add(new Object[] { cota, totalReparte,
			totalVenda });
	    }

	    iCota++;
	}

	return listProdutoEdicaoCota.iterator();
    }

    @DataProvider(name = "getCotaEdicaoBaseUnicaPublicacaoList")
    public static Iterator<Cota[]> getCotaEdicaoBaseUnicaPublicacaoList() {

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
	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO()
		    .getEdicaoRecebidas(cota));

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO()
		    .getByCotaIdProdutoEdicaoId(cota,
			    cota.getEdicoesRecebidas());

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota
			.get(iEdicaoBase);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota
			.getProdutoEdicao();

		produtoEdicao.setReparte(estoqueProdutoCota
			.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota
			.getQuantidadeRecebida().subtract(
				estoqueProdutoCota.getQuantidadeDevolvida()));

		// Produto MALU. -> 12913
		if (produtoEdicao.getIdProduto().equals(new Long(12913))) {

		    edicoesRecebidas.add(produtoEdicao);
		}

		iEdicaoBase++;
	    }

	    cota.setEdicoesRecebidas(edicoesRecebidas);

	    listCotaReturn.add(new Cota[] { cota });
	}

	return listCotaReturn.iterator();
    }
}
