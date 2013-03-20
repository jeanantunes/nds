package br.com.abril.nds.process.dataprovider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.DataProvider;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.EstoqueProdutoCotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.EstoqueProdutoCota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.correcaovendas.CorrecaoVendas;
import br.com.abril.nds.process.medias.Medias;

public abstract class VendaMediaFinalDataProvider {

    @Autowired
    private static CorrecaoVendas correcaoVendas;
    
    @Autowired
    private static Medias medias;
    
    @DataProvider(name = "getCotaParaCalculoList")
    public static Iterator<Cota[]> getCotaQuantidadeEdicoesMenorTresList() throws Exception {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

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
		produtoEdicao.setPeso(BigDecimal.ONE);

		edicoesRecebidas.add(produtoEdicao);

		iEdicaoBase++;
	    }

	    cota.setEdicoesRecebidas(edicoesRecebidas);

	    correcaoVendas.setGenericDTO(cota);
	    correcaoVendas.executar();

	    medias.setGenericDTO(cota);
	    medias.executar();

	    listCotaReturn.add(new Cota[] { cota });

	}

	return listCotaReturn.iterator();
    }

}
