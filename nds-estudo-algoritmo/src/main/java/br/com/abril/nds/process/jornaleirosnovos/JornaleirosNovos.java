package br.com.abril.nds.process.jornaleirosnovos;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.process.ajustecota.AjusteCota;
import br.com.abril.nds.process.correcaovendas.CorrecaoIndividual;
import br.com.abril.nds.process.medias.Medias;
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

    @Autowired
    private Medias medias;

    @Autowired
    private CorrecaoIndividual correcaoIndividual;

    public void executar(CotaEstudo cota) throws Exception {

	cota = cotaDAO.getIndiceAjusteCotaEquivalenteByCota(cota);

	if ((cota.getEquivalente() != null) && (cota.getEquivalente().size() > 0)) {
	    cota.setIndiceAjusteEquivalente(cota.getEquivalente().get(0).getIndiceAjusteEquivalente());
	} 
	if (cota.isNova() && cota.getEdicoesRecebidas() != null && cota.getEdicoesRecebidas().size() <= 3) {
	    
	    BigDecimal totalVendaMediaCorrigidaEquivalente = BigDecimal.ZERO;
	    for (ProdutoEdicaoEstudo produtoEdicao : cota.getEdicoesRecebidas()) {
		if (produtoEdicao.getNumeroEdicao().compareTo(new Long(1)) == 0 || !produtoEdicao.isColecao()) {
		    int iEquivalente = 0;
		    
		    while (iEquivalente < cota.getEquivalente().size()) {
			CotaEstudo cotaEquivalente = cota.getEquivalente().get(iEquivalente);
			cotaEquivalente.setEdicoesRecebidas(produtoEdicaoDAO.getEdicaoRecebidas(cotaEquivalente, produtoEdicao));
			if (cotaEquivalente.getEdicoesRecebidas() != null && !cotaEquivalente.getEdicoesRecebidas().isEmpty()) {
			    int iProdutoEdicaoEquivalente = 0;
			    
			    while (iProdutoEdicaoEquivalente < cotaEquivalente.getEdicoesRecebidas().size()) {
				ProdutoEdicaoEstudo produtoEdicaoEquivalente = cotaEquivalente.getEdicoesRecebidas().get(iProdutoEdicaoEquivalente);
				correcaoIndividual.executar(produtoEdicaoEquivalente);
				iProdutoEdicaoEquivalente++;
			    }
			    medias.executar(cotaEquivalente);
			    BigDecimal vendaMediaCorrigidaEquivalente = cotaEquivalente.getVendaMedia();
			    if (vendaMediaCorrigidaEquivalente.compareTo(BigDecimal.ZERO) == 1)
				totalVendaMediaCorrigidaEquivalente = totalVendaMediaCorrigidaEquivalente.add(vendaMediaCorrigidaEquivalente);
			}
			iEquivalente++;
		    }
		}
	    }

	    if (totalVendaMediaCorrigidaEquivalente.compareTo(BigDecimal.ZERO) == 1) {

		BigDecimal vendaMediaCorrigidaNovo = BigDecimal.ZERO;
		BigDecimal qtdeEquivalente = new BigDecimal(cota.getEquivalente().size());
		vendaMediaCorrigidaNovo = totalVendaMediaCorrigidaEquivalente.divide(qtdeEquivalente, 2, BigDecimal.ROUND_FLOOR);
		if (cota.getIndiceAjusteEquivalente() != null) {
		    vendaMediaCorrigidaNovo = vendaMediaCorrigidaNovo.multiply(cota.getIndiceAjusteEquivalente()).divide(BigDecimal.ONE, 2, BigDecimal.ROUND_FLOOR);
		}

		cota.setVendaMedia(vendaMediaCorrigidaNovo);
	    }
	}
    }
}
