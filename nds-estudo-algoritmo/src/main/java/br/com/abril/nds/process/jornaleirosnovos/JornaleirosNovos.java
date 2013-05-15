package br.com.abril.nds.process.jornaleirosnovos;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.vendamediafinal.VendaMediaFinal;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link AjusteCota} Próximo Processo: {@link VendaMediaFinal} </p>
 */
@Component
public class JornaleirosNovos {

    @Autowired
    private CotaDAO cotaDAO;

    @Autowired
    private ProdutoEdicaoDAO produtoEdicaoDAO;


    public void executar(EstudoTransient estudo) throws Exception {

	HashMap<Long, CotaEstudo> mapCotas = new HashMap<>();
	for (CotaEstudo cota : estudo.getCotas()) {
	    mapCotas.put(cota.getId(), cota);
	}
	for (CotaEstudo cota : estudo.getCotasExcluidas()) {
	    mapCotas.put(cota.getId(), cota);
	}
	for (CotaEstudo cota : estudo.getCotas()) {
	    if (cota.isNova() && cota.getEdicoesRecebidas() != null && cota.getEdicoesRecebidas().size() <= 3) {
		List<CotaEstudo> equivalentes = cotaDAO.getIndiceAjusteCotaEquivalenteByCota(cota);

		if ((equivalentes != null) && (equivalentes.size() > 0)) {
		    if (equivalentes.get(0).getIndiceAjusteEquivalente() == null) {
			cota.setIndiceAjusteEquivalente(BigDecimal.ONE);
		    } else {
			cota.setIndiceAjusteEquivalente(equivalentes.get(0).getIndiceAjusteEquivalente());
		    }

		    BigDecimal somaVendaMediaEquiv = BigDecimal.ZERO;
		    BigDecimal qtde = BigDecimal.ZERO;
		    for (CotaEstudo equiv : equivalentes) {
			if (mapCotas.get(equiv.getId()) != null) {
			    somaVendaMediaEquiv = somaVendaMediaEquiv.add(mapCotas.get(equiv.getId()).getVendaMedia());
			    qtde = qtde.add(BigDecimal.ONE);
			}
		    }
		    cota.setVendaMedia(somaVendaMediaEquiv.divide(qtde, 0, BigDecimal.ROUND_FLOOR).multiply(cota.getIndiceAjusteEquivalente()));
		}



		
//		BigDecimal totalVendaMediaCorrigidaEquivalente = BigDecimal.ZERO;
//		for (ProdutoEdicaoEstudo produtoEdicao : cota.getEdicoesRecebidas()) {
//		    if (produtoEdicao.getNumeroEdicao().compareTo(new Long(1)) == 0 || !produtoEdicao.isColecao()) {
//			for (CotaEstudo cotaEquivalente : cota.getEquivalente()) {
//			    mapCotas.get()
//			    cotaEquivalente.setEdicoesRecebidas(produtoEdicaoDAO.getEdicaoRecebidas(cotaEquivalente, produtoEdicao));
//			    if (cotaEquivalente.getEdicoesRecebidas() != null && !cotaEquivalente.getEdicoesRecebidas().isEmpty()) {
//				for (ProdutoEdicaoEstudo produtoEdicaoEquivalente : cotaEquivalente.getEdicoesRecebidas()) {
//				    correcaoIndividual.executar(produtoEdicaoEquivalente);
//				}
//				medias.executar(cotaEquivalente);
//				BigDecimal vendaMediaCorrigidaEquivalente = cotaEquivalente.getVendaMedia();
//				if (vendaMediaCorrigidaEquivalente.compareTo(BigDecimal.ZERO) == 1)
//				    totalVendaMediaCorrigidaEquivalente = totalVendaMediaCorrigidaEquivalente.add(vendaMediaCorrigidaEquivalente);
//			    }
//			}
//		    }
//		}
//
//		if (totalVendaMediaCorrigidaEquivalente.compareTo(BigDecimal.ZERO) == 1) {
//
//		    BigDecimal vendaMediaCorrigidaNovo = BigDecimal.ZERO;
//		    BigDecimal qtdeEquivalente = new BigDecimal(cota.getEquivalente().size());
//		    vendaMediaCorrigidaNovo = totalVendaMediaCorrigidaEquivalente.divide(qtdeEquivalente, 2, BigDecimal.ROUND_FLOOR);
//		    if (cota.getIndiceAjusteEquivalente() != null) {
//			vendaMediaCorrigidaNovo = vendaMediaCorrigidaNovo.multiply(cota.getIndiceAjusteEquivalente()).divide(BigDecimal.ONE, 2, BigDecimal.ROUND_FLOOR);
//		    }
//
//		    cota.setVendaMedia(vendaMediaCorrigidaNovo);
		}
	    }
	}
    }
