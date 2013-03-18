package br.com.abril.nds.process.dataprovider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

<<<<<<< HEAD
import org.testng.ITestContext;
=======
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> branch 'master' of https://adenilton@bitbucket.org/pedroxs/nds.git
import org.testng.annotations.DataProvider;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.TipoAjusteReparte;
import br.com.abril.nds.model.TipoSegmentoProduto;

<<<<<<< HEAD
public abstract class AjusteCotaDataProvider extends NDSDataProvider {
=======
// FIXME: é preciso remover os comentários do código e solucionar o problema
public abstract class AjusteCotaDataProvider {
>>>>>>> branch 'master' of https://adenilton@bitbucket.org/pedroxs/nds.git

    @Autowired
    private static CotaDAO cotaDAO;
    
    @DataProvider(name = "getCotaSemIndiceAjusteSegmentoList")
    public static Iterator<Cota[]> getCotaSemIndiceAjusteSegmentoList(ITestContext context) {

	List<Long> listParamCotaId = getParamCotaId(context);

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = cotaDAO.getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    Cota cota = itCota.next();

	    if (!listParamCotaId.isEmpty() && !listParamCotaId.contains(cota.getId())) {
		itCota.remove();
		continue;
	    }

	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    BigDecimal ajusteAplicado = BigDecimal.ONE;
//	    BigDecimal ajusteAplicado = cotaDAO.getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, null, new TipoAjusteReparte[] {
//		    TipoAjusteReparte.AJUSTE_ENCALHE_MAX, TipoAjusteReparte.AJUSTE_HISTORICO, TipoAjusteReparte.AJUSTE_VENDA_MEDIA });

	    if (ajusteAplicado != null && ajusteAplicado.compareTo(BigDecimal.ZERO) == 1) {
		listCotaReturn.add(new Cota[] { cota });
	    }

	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getCotaComIndiceAjusteSegmentoList")
    public static Iterator<Cota[]> getCotaComIndiceAjusteSegmentoList(ITestContext context) {

	List<Long> listParamCotaId = getParamCotaId(context);

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    Cota cota = itCota.next();

	    if (!listParamCotaId.isEmpty() && !listParamCotaId.contains(cota.getId())) {
		itCota.remove();
		continue;
	    }

	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    List<ProdutoEdicao> listProdutoEdicao = cota.getEdicoesRecebidas();

	    if (listProdutoEdicao != null && !listProdutoEdicao.isEmpty()) {

//		TipoSegmentoProduto tipoSegmentoProduto = listProdutoEdicao.iterator().next().getTipoSegmentoProduto();

		BigDecimal ajusteAplicado = BigDecimal.ONE;
//		BigDecimal ajusteAplicado = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, tipoSegmentoProduto,
//			new TipoAjusteReparte[] { TipoAjusteReparte.AJUSTE_SEGMENTO });

		if (ajusteAplicado != null && ajusteAplicado.compareTo(BigDecimal.ZERO) == 1) {
		    listCotaReturn.add(new Cota[] { cota });
		}
	    }

	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getCotaComIndiceAjusteSegmentoMenorList")
    public static Iterator<Cota[]> getCotaComIndiceAjusteSegmentoMenorList(ITestContext context) {

	List<Long> listParamCotaId = getParamCotaId(context);

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    Cota cota = itCota.next();

	    if (!listParamCotaId.isEmpty() && !listParamCotaId.contains(cota.getId())) {
		itCota.remove();
		continue;
	    }

	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    List<ProdutoEdicao> listProdutoEdicao = cota.getEdicoesRecebidas();

	    if (listProdutoEdicao != null && !listProdutoEdicao.isEmpty()) {

<<<<<<< HEAD
		TipoSegmentoProduto tipoSegmentoProduto = listProdutoEdicao.iterator().next().getTipoSegmentoProduto();

		BigDecimal ajusteAplicado = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, null, new TipoAjusteReparte[] {
			TipoAjusteReparte.AJUSTE_ENCALHE_MAX, TipoAjusteReparte.AJUSTE_HISTORICO, TipoAjusteReparte.AJUSTE_VENDA_MEDIA });

		BigDecimal ajusteAplicadoSegmento = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, tipoSegmentoProduto,
			new TipoAjusteReparte[] { TipoAjusteReparte.AJUSTE_SEGMENTO });

=======
//		TipoSegmentoProduto tipoSegmentoProduto = listProdutoEdicao.iterator().next().getTipoSegmentoProduto();
		BigDecimal ajusteAplicado = BigDecimal.ONE;
//		BigDecimal ajusteAplicado = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, null, new TipoAjusteReparte[] {
//			TipoAjusteReparte.AJUSTE_ENCALHE_MAX, TipoAjusteReparte.AJUSTE_HISTORICO, TipoAjusteReparte.AJUSTE_VENDA_MEDIA });
		BigDecimal ajusteAplicadoSegmento = BigDecimal.ONE;
//		BigDecimal ajusteAplicadoSegmento = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, tipoSegmentoProduto,
//			new TipoAjusteReparte[] { TipoAjusteReparte.AJUSTE_SEGMENTO });
		
>>>>>>> branch 'master' of https://adenilton@bitbucket.org/pedroxs/nds.git
		if (ajusteAplicado != null && ajusteAplicadoSegmento != null && ajusteAplicado.compareTo(ajusteAplicadoSegmento) == 1) {
		    listCotaReturn.add(new Cota[] { cota });
		}
	    }

	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getCotaComIndiceAjusteMenorList")
    public static Iterator<Cota[]> getCotaComIndiceAjusteMenorList(ITestContext context) {

	List<Long> listParamCotaId = getParamCotaId(context);

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    Cota cota = itCota.next();

	    if (!listParamCotaId.isEmpty() && !listParamCotaId.contains(cota.getId())) {
		itCota.remove();
		continue;
	    }

	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    List<ProdutoEdicao> listProdutoEdicao = cota.getEdicoesRecebidas();

	    if (listProdutoEdicao != null && !listProdutoEdicao.isEmpty()) {

<<<<<<< HEAD
		TipoSegmentoProduto tipoSegmentoProduto = listProdutoEdicao.iterator().next().getTipoSegmentoProduto();

		BigDecimal ajusteAplicado = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, null, new TipoAjusteReparte[] {
			TipoAjusteReparte.AJUSTE_ENCALHE_MAX, TipoAjusteReparte.AJUSTE_HISTORICO, TipoAjusteReparte.AJUSTE_VENDA_MEDIA });

		BigDecimal ajusteAplicadoSegmento = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, tipoSegmentoProduto,
			new TipoAjusteReparte[] { TipoAjusteReparte.AJUSTE_SEGMENTO });

=======
//		TipoSegmentoProduto tipoSegmentoProduto = listProdutoEdicao.iterator().next().getTipoSegmentoProduto();
		BigDecimal ajusteAplicado = BigDecimal.ONE;
//		BigDecimal ajusteAplicado = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, null, new TipoAjusteReparte[] {
//			TipoAjusteReparte.AJUSTE_ENCALHE_MAX, TipoAjusteReparte.AJUSTE_HISTORICO, TipoAjusteReparte.AJUSTE_VENDA_MEDIA });
		BigDecimal ajusteAplicadoSegmento = BigDecimal.ONE;
//		BigDecimal ajusteAplicadoSegmento = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, tipoSegmentoProduto,
//			new TipoAjusteReparte[] { TipoAjusteReparte.AJUSTE_SEGMENTO });
		
>>>>>>> branch 'master' of https://adenilton@bitbucket.org/pedroxs/nds.git
		if (ajusteAplicado != null && ajusteAplicadoSegmento != null && ajusteAplicado.compareTo(ajusteAplicadoSegmento) == -1) {
		    listCotaReturn.add(new Cota[] { cota });
		}
	    }

	}

	return listCotaReturn.iterator();
    }

}
