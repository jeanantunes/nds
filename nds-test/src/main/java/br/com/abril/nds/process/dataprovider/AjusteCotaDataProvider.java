package br.com.abril.nds.process.dataprovider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.DataProvider;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;

// FIXME: é preciso remover os comentários do código e solucionar o problema
public abstract class AjusteCotaDataProvider {

    @Autowired
    private static CotaDAO cotaDAO;
    
    @DataProvider(name = "getCotaSemIndiceAjusteSegmentoList")
    public static Iterator<Cota[]> getCotaSemIndiceAjusteSegmentoList() {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = cotaDAO.getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    Cota cota = itCota.next();
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
    public static Iterator<Cota[]> getCotaComIndiceAjusteSegmentoList() {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    Cota cota = itCota.next();
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
    public static Iterator<Cota[]> getCotaComIndiceAjusteSegmentoMenorList() {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    Cota cota = itCota.next();
	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    List<ProdutoEdicao> listProdutoEdicao = cota.getEdicoesRecebidas();

	    if (listProdutoEdicao != null && !listProdutoEdicao.isEmpty()) {

//		TipoSegmentoProduto tipoSegmentoProduto = listProdutoEdicao.iterator().next().getTipoSegmentoProduto();
		BigDecimal ajusteAplicado = BigDecimal.ONE;
//		BigDecimal ajusteAplicado = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, null, new TipoAjusteReparte[] {
//			TipoAjusteReparte.AJUSTE_ENCALHE_MAX, TipoAjusteReparte.AJUSTE_HISTORICO, TipoAjusteReparte.AJUSTE_VENDA_MEDIA });
		BigDecimal ajusteAplicadoSegmento = BigDecimal.ONE;
//		BigDecimal ajusteAplicadoSegmento = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, tipoSegmentoProduto,
//			new TipoAjusteReparte[] { TipoAjusteReparte.AJUSTE_SEGMENTO });
		
		if (ajusteAplicado != null && ajusteAplicadoSegmento != null && ajusteAplicado.compareTo(ajusteAplicadoSegmento) == 1) {
		    listCotaReturn.add(new Cota[] { cota });
		}
	    }

	}

	return listCotaReturn.iterator();
    }
    
    @DataProvider(name = "getCotaComIndiceAjusteMenorList")
    public static Iterator<Cota[]> getCotaComIndiceAjusteMenorList() {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    Cota cota = itCota.next();
	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));

	    List<ProdutoEdicao> listProdutoEdicao = cota.getEdicoesRecebidas();

	    if (listProdutoEdicao != null && !listProdutoEdicao.isEmpty()) {

//		TipoSegmentoProduto tipoSegmentoProduto = listProdutoEdicao.iterator().next().getTipoSegmentoProduto();
		BigDecimal ajusteAplicado = BigDecimal.ONE;
//		BigDecimal ajusteAplicado = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, null, new TipoAjusteReparte[] {
//			TipoAjusteReparte.AJUSTE_ENCALHE_MAX, TipoAjusteReparte.AJUSTE_HISTORICO, TipoAjusteReparte.AJUSTE_VENDA_MEDIA });
		BigDecimal ajusteAplicadoSegmento = BigDecimal.ONE;
//		BigDecimal ajusteAplicadoSegmento = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, tipoSegmentoProduto,
//			new TipoAjusteReparte[] { TipoAjusteReparte.AJUSTE_SEGMENTO });
		
		if (ajusteAplicado != null && ajusteAplicadoSegmento != null && ajusteAplicado.compareTo(ajusteAplicadoSegmento) == -1) {
		    listCotaReturn.add(new Cota[] { cota });
		}
	    }

	}

	return listCotaReturn.iterator();
    }

}
