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

    @DataProvider(name = "getCotaEdicaoNaoPrimeiraEdicaoList")
    public static Iterator<Cota[]> getCotaEdicaoNaoPrimeiraEdicaoList() {

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

		if (!produtoEdicao.getNumeroEdicao().equals(new Long(1))) {
		    edicoesRecebidas.add(produtoEdicao);
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

    @DataProvider(name = "getCotaEdicaoPrimeiraEdicaoList")
    public static Iterator<Cota[]> getCotaEdicaoPrimeiraEdicaoList() {

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

		if (produtoEdicao.getNumeroEdicao().equals(new Long(1))) {
		    edicoesRecebidas.add(produtoEdicao);
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

    @DataProvider(name = "getCotaEdicaoPrimeiraEdicaoColecaoList")
    public static Iterator<Cota[]> getCotaEdicaoPrimeiraEdicaoColecaoList() {

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

		if (produtoEdicao.getNumeroEdicao().equals(new Long(1)) && produtoEdicao.isColecao()) {
		    edicoesRecebidas.add(produtoEdicao);
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

    @DataProvider(name = "getCotaEdicaoNaoPrimeiraEdicaoNaoColecaoList")
    public static Iterator<Cota[]> getCotaEdicaoNaoPrimeiraEdicaoNaoColecaoList() {

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

		if (!produtoEdicao.getNumeroEdicao().equals(new Long(1)) && !produtoEdicao.isColecao()) {
		    edicoesRecebidas.add(produtoEdicao);
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

    @DataProvider(name = "getCotaEdicaoFechadasList")
    public static Iterator<Cota[]> getCotaEdicaoFechadasList() {

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

		if (!produtoEdicao.isEdicaoAberta()) {
		    edicoesRecebidas.add(produtoEdicao);
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
