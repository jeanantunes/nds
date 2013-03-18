package br.com.abril.nds.process.dataprovider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.EstoqueProdutoCotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;
import br.com.abril.nds.model.ProdutoEdicao;

public abstract class CorrecaoVendasDataProvider extends NDSDataProvider {

    @DataProvider(name = "getCotaComEdicaoNaoPrimeiraEdicaoList")
    public static Iterator<Cota[]> getCotaComEdicaoNaoPrimeiraEdicaoList(ITestContext context) {

	List<Long> listParamCotaId = getParamCotaId(context);

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    List<Boolean> hasFirstEdition = new ArrayList<Boolean>();

	    List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();

	    Cota cota = itCota.next();

	    if (!listParamCotaId.isEmpty() && !listParamCotaId.contains(cota.getId())) {
		itCota.remove();
		continue;
	    }

	    List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();

		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));

		if (produtoEdicao.getNumeroEdicao().compareTo(new Long(1)) == 1) {
		    hasFirstEdition.add(Boolean.TRUE);
		}

		edicoesRecebidas.add(produtoEdicao);

		iEdicaoBase++;
	    }

	    if (hasFirstEdition.contains(Boolean.TRUE)) {
		cota.setEdicoesRecebidas(edicoesRecebidas);
		listCotaReturn.add(new Cota[] { cota });
	    }

	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getCotaComEdicaoFasciculoColecaoList")
    public static Iterator<Cota[]> getCotaComEdicaoFasciculoColecaoList(ITestContext context) {

	List<Long> listParamCotaId = getParamCotaId(context);

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    List<Boolean> hasFirstEdition = new ArrayList<Boolean>();

	    List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();

	    Cota cota = itCota.next();

	    if (!listParamCotaId.isEmpty() && !listParamCotaId.contains(cota.getId())) {
		itCota.remove();
		continue;
	    }

	    List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();

		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));

		if (produtoEdicao.isColecao()) {
		    hasFirstEdition.add(Boolean.TRUE);
		}

		edicoesRecebidas.add(produtoEdicao);

		iEdicaoBase++;
	    }

	    if (hasFirstEdition.contains(Boolean.TRUE)) {
		cota.setEdicoesRecebidas(edicoesRecebidas);
		listCotaReturn.add(new Cota[] { cota });
	    }

	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getCotaComEdicaoPrimeiraEdicaoList")
    public static Iterator<Cota[]> getCotaComEdicaoPrimeiraEdicaoList(ITestContext context) {

	List<Long> listParamCotaId = getParamCotaId(context);

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    List<Boolean> hasFirstEdition = new ArrayList<Boolean>();

	    List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();

	    Cota cota = itCota.next();

	    if (!listParamCotaId.isEmpty() && !listParamCotaId.contains(cota.getId())) {
		itCota.remove();
		continue;
	    }

	    List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();

		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));

		if (produtoEdicao.getNumeroEdicao().compareTo(new Long(1)) == 0) {
		    hasFirstEdition.add(Boolean.TRUE);
		}

		edicoesRecebidas.add(produtoEdicao);

		iEdicaoBase++;
	    }

	    if (hasFirstEdition.contains(Boolean.TRUE)) {
		cota.setEdicoesRecebidas(edicoesRecebidas);
		listCotaReturn.add(new Cota[] { cota });
	    }

	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getCotaComEdicaoNaoFasciculoColecaoList")
    public static Iterator<Cota[]> getCotaComEdicaoNaoFasciculoColecaoList(ITestContext context) {

	List<Long> listParamCotaId = getParamCotaId(context);

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    List<Boolean> hasFirstEdition = new ArrayList<Boolean>();

	    List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();

	    Cota cota = itCota.next();

	    if (!listParamCotaId.isEmpty() && !listParamCotaId.contains(cota.getId())) {
		itCota.remove();
		continue;
	    }

	    List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();

		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));

		if (!produtoEdicao.isColecao()) {
		    hasFirstEdition.add(Boolean.TRUE);
		}

		edicoesRecebidas.add(produtoEdicao);

		iEdicaoBase++;
	    }

	    if (hasFirstEdition.contains(Boolean.TRUE)) {
		cota.setEdicoesRecebidas(edicoesRecebidas);
		listCotaReturn.add(new Cota[] { cota });
	    }

	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getCotaComEdicaoNaoFechadaList")
    public static Iterator<Cota[]> getCotaComEdicaoNaoFechadaList(ITestContext context) {

	List<Long> listParamCotaId = getParamCotaId(context);

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    List<Boolean> hasFirstEdition = new ArrayList<Boolean>();

	    List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();

	    Cota cota = itCota.next();

	    if (!listParamCotaId.isEmpty() && !listParamCotaId.contains(cota.getId())) {
		itCota.remove();
		continue;
	    }

	    List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();

		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));

		if (produtoEdicao.isEdicaoAberta()) {
		    hasFirstEdition.add(Boolean.TRUE);
		}

		edicoesRecebidas.add(produtoEdicao);

		iEdicaoBase++;
	    }

	    if (hasFirstEdition.contains(Boolean.TRUE)) {
		cota.setEdicoesRecebidas(edicoesRecebidas);
		listCotaReturn.add(new Cota[] { cota });
	    }

	}

	return listCotaReturn.iterator();
    }
    
    @DataProvider(name = "getCotaComEdicaoFechadaList")
    public static Iterator<Cota[]> getCotaComEdicaoFechadaList(ITestContext context) {

	List<Long> listParamCotaId = getParamCotaId(context);

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    List<Boolean> hasFirstEdition = new ArrayList<Boolean>();

	    List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();

	    Cota cota = itCota.next();

	    if (!listParamCotaId.isEmpty() && !listParamCotaId.contains(cota.getId())) {
		itCota.remove();
		continue;
	    }

	    List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);

	    List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);

	    int iEdicaoBase = 0;
	    while (iEdicaoBase < listEstoqueProdutoCota.size()) {

		EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);

		ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();

		produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
		produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));

		if (!produtoEdicao.isEdicaoAberta()) {
		    hasFirstEdition.add(Boolean.TRUE);
		}

		edicoesRecebidas.add(produtoEdicao);

		iEdicaoBase++;
	    }

	    if (hasFirstEdition.contains(Boolean.TRUE)) {
		cota.setEdicoesRecebidas(edicoesRecebidas);
		listCotaReturn.add(new Cota[] { cota });
	    }

	}

	return listCotaReturn.iterator();
    }

    // @DataProvider(name = "getCotaComEdicaoPrimeiraEdicaoOuNaoColecaoList")
    // public static Iterator<Cota[]> getCotaComEdicaoPrimeiraEdicaoOuNaoColecaoList() {
    //
    // List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();
    //
    // Estudo estudo = new Estudo();
    // List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();
    // estudo.setCotas(listCota);
    //
    // Iterator<Cota> itCota = listCota.iterator();
    //
    // while (itCota.hasNext()) {
    //
    // List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();
    //
    // Cota cota = itCota.next();
    //
    // List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);
    //
    // List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);
    //
    // int iEdicaoBase = 0;
    // while (iEdicaoBase < listEstoqueProdutoCota.size()) {
    //
    // EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);
    //
    // ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
    //
    // produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
    // produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));
    //
    // if (produtoEdicao.getNumeroEdicao().compareTo(new Long(1)) == 0 || !produtoEdicao.isColecao()) {
    // edicoesRecebidas.add(produtoEdicao);
    // }
    //
    // iEdicaoBase++;
    // }
    //
    // if (edicoesRecebidas != null && !edicoesRecebidas.isEmpty()) {
    // cota.setEdicoesRecebidas(produtoEdicaoList);
    // listCotaReturn.add(new Cota[] { cota });
    // }
    //
    // }
    //
    // return listCotaReturn.iterator();
    // }
    //
    // @DataProvider(name = "getCotaEdicaoPrimeiraEdicaoColecaoList")
    // public static Iterator<Cota[]> getCotaEdicaoPrimeiraEdicaoColecaoList() {
    //
    // List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();
    //
    // Estudo estudo = new Estudo();
    // List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();
    // estudo.setCotas(listCota);
    //
    // Iterator<Cota> itCota = listCota.iterator();
    //
    // while (itCota.hasNext()) {
    //
    // List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();
    //
    // Cota cota = itCota.next();
    //
    // List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);
    //
    // List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);
    //
    // int iEdicaoBase = 0;
    // while (iEdicaoBase < listEstoqueProdutoCota.size()) {
    //
    // EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);
    //
    // ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
    //
    // produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
    // produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));
    //
    // if (produtoEdicao.getNumeroEdicao().equals(new Long(1)) && produtoEdicao.isColecao()) {
    // edicoesRecebidas.add(produtoEdicao);
    // }
    //
    // iEdicaoBase++;
    // }
    //
    // if (edicoesRecebidas != null && !edicoesRecebidas.isEmpty()) {
    // cota.setEdicoesRecebidas(edicoesRecebidas);
    // listCotaReturn.add(new Cota[] { cota });
    // }
    //
    // }
    //
    // return listCotaReturn.iterator();
    // }
    //
    // @DataProvider(name = "getCotaEdicaoNaoPrimeiraEdicaoNaoColecaoList")
    // public static Iterator<Cota[]> getCotaEdicaoNaoPrimeiraEdicaoNaoColecaoList() {
    //
    // List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();
    //
    // Estudo estudo = new Estudo();
    // List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();
    // estudo.setCotas(listCota);
    //
    // Iterator<Cota> itCota = listCota.iterator();
    //
    // while (itCota.hasNext()) {
    //
    // List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();
    //
    // Cota cota = itCota.next();
    //
    // List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);
    //
    // List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);
    //
    // int iEdicaoBase = 0;
    // while (iEdicaoBase < listEstoqueProdutoCota.size()) {
    //
    // EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);
    //
    // ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
    //
    // produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
    // produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));
    //
    // if (!produtoEdicao.getNumeroEdicao().equals(new Long(1)) && !produtoEdicao.isColecao()) {
    // edicoesRecebidas.add(produtoEdicao);
    // }
    //
    // iEdicaoBase++;
    // }
    //
    // if (edicoesRecebidas != null && !edicoesRecebidas.isEmpty()) {
    // cota.setEdicoesRecebidas(edicoesRecebidas);
    // listCotaReturn.add(new Cota[] { cota });
    // }
    //
    // }
    //
    // return listCotaReturn.iterator();
    // }
    //
    // @DataProvider(name = "getCotaEdicaoFechadasList")
    // public static Iterator<Cota[]> getCotaEdicaoFechadasList() {
    //
    // List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();
    //
    // Estudo estudo = new Estudo();
    // List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();
    // estudo.setCotas(listCota);
    //
    // Iterator<Cota> itCota = listCota.iterator();
    //
    // while (itCota.hasNext()) {
    //
    // List<ProdutoEdicao> edicoesRecebidas = new ArrayList<ProdutoEdicao>();
    //
    // Cota cota = itCota.next();
    //
    // List<ProdutoEdicao> produtoEdicaoList = new ProdutoEdicaoDAO().getEdicaoRecebidas(cota);
    //
    // List<EstoqueProdutoCota> listEstoqueProdutoCota = new EstoqueProdutoCotaDAO().getByCotaIdProdutoEdicaoId(cota, produtoEdicaoList);
    //
    // int iEdicaoBase = 0;
    // while (iEdicaoBase < listEstoqueProdutoCota.size()) {
    //
    // EstoqueProdutoCota estoqueProdutoCota = listEstoqueProdutoCota.get(iEdicaoBase);
    //
    // ProdutoEdicao produtoEdicao = estoqueProdutoCota.getProdutoEdicao();
    //
    // produtoEdicao.setReparte(estoqueProdutoCota.getQuantidadeRecebida());
    // produtoEdicao.setVenda(estoqueProdutoCota.getQuantidadeRecebida().subtract(estoqueProdutoCota.getQuantidadeDevolvida()));
    //
    // if (!produtoEdicao.isEdicaoAberta()) {
    // edicoesRecebidas.add(produtoEdicao);
    // }
    //
    // iEdicaoBase++;
    // }
    //
    // if (edicoesRecebidas != null && !edicoesRecebidas.isEmpty()) {
    // cota.setEdicoesRecebidas(edicoesRecebidas);
    // listCotaReturn.add(new Cota[] { cota });
    // }
    //
    // }
    //
    // return listCotaReturn.iterator();
    // }
}
