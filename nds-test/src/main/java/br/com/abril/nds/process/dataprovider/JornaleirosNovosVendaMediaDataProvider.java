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
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.correcaovendas.CorrecaoIndividual;
import br.com.abril.nds.process.medias.Medias;

public abstract class JornaleirosNovosVendaMediaDataProvider {

    @Autowired
    private static Medias medias;
    
    @Autowired
    private static CorrecaoIndividual correcaoIndividual;
    
    @DataProvider(name = "getCotaNovaComQtdeEdicaoBaseMenorIgualTresComEquivalenteVendaMediaCorrigidaMaiorZeroList")
    public static Iterator<Cota[]> getCotaNovaComQtdeEdicaoBaseMenorIgualTresComEquivalenteVendaMediaCorrigidaMaiorZeroList() throws Exception {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	Estudo estudo = new Estudo();
	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();
	estudo.setCotas(listCota);

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    Cota cota = itCota.next();
	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));
	    cota = new CotaDAO().getIndiceAjusteCotaEquivalenteByCota(cota);

	    if (cota.isNova() && cota.getEdicoesRecebidas() != null && cota.getEdicoesRecebidas().size() <= 3) {

		boolean hasVendaMediaMaiorZero = false;

		List<Cota> listCotaEquivalente = cota.getEquivalente();
		Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

		while (itProdutoEdicao.hasNext()) {

		    ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		    correcaoIndividual.setGenericDTO(produtoEdicao);
		    correcaoIndividual.executar();

		    int iCotaEquivalente = 0;
		    while (iCotaEquivalente < listCotaEquivalente.size()) {

			Cota cotaEquivalente = listCotaEquivalente.get(iCotaEquivalente);

			cotaEquivalente.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cotaEquivalente, produtoEdicao));

			if (cotaEquivalente.getEdicoesRecebidas() != null && !cotaEquivalente.getEdicoesRecebidas().isEmpty()) {

			    int iProdutoEdicaoEquivalente = 0;
			    while (iProdutoEdicaoEquivalente < cotaEquivalente.getEdicoesRecebidas().size()) {

				ProdutoEdicao produtoEdicaoEquivalente = cotaEquivalente.getEdicoesRecebidas().get(iProdutoEdicaoEquivalente);

				correcaoIndividual.setGenericDTO(produtoEdicaoEquivalente);
				correcaoIndividual.executar();

				iProdutoEdicaoEquivalente++;
			    }

			    medias.setGenericDTO(cotaEquivalente);
			    medias.executar();

			}

			if (cotaEquivalente.getVendaMedia() != null && cotaEquivalente.getVendaMedia().compareTo(BigDecimal.ZERO) == 1) {
			    hasVendaMediaMaiorZero = true;
			    break;
			}

			iCotaEquivalente++;
		    }

		    if (hasVendaMediaMaiorZero) {
			listCotaReturn.add(new Cota[] { cota });
			break;
		    }
		}

		medias.setGenericDTO(cota);
		medias.executar();
	    }
	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getCotaNovaComQtdeEdicaoBaseMaiorTresList")
    public static Iterator<Cota[]> getCotaNovaComQtdeEdicaoBaseMaiorTresList() throws Exception {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	Estudo estudo = new Estudo();
	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();
	estudo.setCotas(listCota);

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    Cota cota = itCota.next();
	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));
	    cota = new CotaDAO().getIndiceAjusteCotaEquivalenteByCota(cota);

	    if (cota.isNova() && cota.getEdicoesRecebidas() != null && cota.getEdicoesRecebidas().size() > 3) {

		List<Cota> listCotaEquivalente = cota.getEquivalente();
		Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

		while (itProdutoEdicao.hasNext()) {

		    ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		    correcaoIndividual.setGenericDTO(produtoEdicao);
		    correcaoIndividual.executar();

		    int iCotaEquivalente = 0;
		    while (iCotaEquivalente < listCotaEquivalente.size()) {

			Cota cotaEquivalente = listCotaEquivalente.get(iCotaEquivalente);

			cotaEquivalente.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cotaEquivalente, produtoEdicao));

			if (cotaEquivalente.getEdicoesRecebidas() != null && !cotaEquivalente.getEdicoesRecebidas().isEmpty()) {

			    int iProdutoEdicaoEquivalente = 0;
			    while (iProdutoEdicaoEquivalente < cotaEquivalente.getEdicoesRecebidas().size()) {

				ProdutoEdicao produtoEdicaoEquivalente = cotaEquivalente.getEdicoesRecebidas().get(iProdutoEdicaoEquivalente);

				correcaoIndividual.setGenericDTO(produtoEdicaoEquivalente);
				correcaoIndividual.executar();

				iProdutoEdicaoEquivalente++;
			    }

			    medias.setGenericDTO(cotaEquivalente);
			    medias.executar();

			}

			iCotaEquivalente++;
		    }
		}

		medias.setGenericDTO(cota);
		medias.executar();

		listCotaReturn.add(new Cota[] { cota });
	    }
	}

	return listCotaReturn.iterator();
    }

    @DataProvider(name = "getCotaNovaComQtdeEdicaoBaseMenorIgualTresSemEquivalenteVendaMediaCorrigidaMaiorZeroList")
    public static Iterator<Cota[]> getCotaNovaComQtdeEdicaoBaseMenorIgualTresSemEquivalenteVendaMediaCorrigidaMaiorZeroList() throws Exception {

	List<Cota[]> listCotaReturn = new ArrayList<Cota[]>();

	List<Cota> listCota = new CotaDAO().getCotaWithEstoqueProdutoCota();

	Iterator<Cota> itCota = listCota.iterator();

	while (itCota.hasNext()) {

	    Cota cota = itCota.next();
	    cota.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cota));
	    cota = new CotaDAO().getIndiceAjusteCotaEquivalenteByCota(cota);
	    
	    if (cota.isNova() && cota.getEdicoesRecebidas() != null && cota.getEdicoesRecebidas().size() <= 3) {

		List<Cota> listCotaEquivalente = cota.getEquivalente();
		Iterator<ProdutoEdicao> itProdutoEdicao = cota.getEdicoesRecebidas().iterator();

		List<Boolean> hasVendaMediaMaiorZero = new ArrayList<Boolean>();

		while (itProdutoEdicao.hasNext()) {

		    ProdutoEdicao produtoEdicao = itProdutoEdicao.next();

		    correcaoIndividual.setGenericDTO(produtoEdicao);
		    correcaoIndividual.executar();

		    int iCotaEquivalente = 0;
		    while (iCotaEquivalente < listCotaEquivalente.size()) {

			Cota cotaEquivalente = listCotaEquivalente.get(iCotaEquivalente);
			
			cotaEquivalente.setEdicoesRecebidas(new ProdutoEdicaoDAO().getEdicaoRecebidas(cotaEquivalente, produtoEdicao));

			if (cotaEquivalente.getEdicoesRecebidas() != null && !cotaEquivalente.getEdicoesRecebidas().isEmpty()) {

			    int iProdutoEdicaoEquivalente = 0;
			    while (iProdutoEdicaoEquivalente < cotaEquivalente.getEdicoesRecebidas().size()) {

				ProdutoEdicao produtoEdicaoEquivalente = cotaEquivalente.getEdicoesRecebidas().get(iProdutoEdicaoEquivalente);

				correcaoIndividual.setGenericDTO(produtoEdicaoEquivalente);
				correcaoIndividual.executar();

				iProdutoEdicaoEquivalente++;
			    }

			    medias.setGenericDTO(cotaEquivalente);
			    medias.executar();

			}

			if (cotaEquivalente.getVendaMedia() != null && cotaEquivalente.getVendaMedia().compareTo(BigDecimal.ZERO) == 1) {
			    hasVendaMediaMaiorZero.add(Boolean.TRUE);
			}

			iCotaEquivalente++;
		    }
		}

		medias.setGenericDTO(cota);
		medias.executar();

		if (!hasVendaMediaMaiorZero.contains(Boolean.TRUE)) {
		    listCotaReturn.add(new Cota[] { cota });
		}
	    }
	}

	return listCotaReturn.iterator();
    }

}
