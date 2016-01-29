package br.com.abril.nds.process.jornaleirosnovos;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
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

    public void executar(EstudoTransient estudo) throws Exception {
    	
	//TipoClassificacaoProduto 31 == CAPA/FICH/BOX
	if (((estudo.getProdutoEdicaoEstudo().getNumeroEdicao().compareTo(Long.valueOf(1)) == 0) || (!estudo.getProdutoEdicaoEstudo().isColecao())) && (estudo.getProdutoEdicaoEstudo().getTipoClassificacaoProduto().getId() != 31L)) {
	    HashMap<Long, CotaEstudo> mapCotas = new HashMap<>(); 
	    
	    for (CotaEstudo cota : estudo.getCotas()) {
	    	mapCotas.put(cota.getId(), cota);
	    }
	    
	    for (CotaEstudo cota : estudo.getCotas()) {
		if (cota.getClassificacao().equals(ClassificacaoCota.CotaNova) && cota.getEdicoesRecebidas() != null
			&& cota.getEdicoesRecebidas().size() <= 3) {
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
			if (qtde.compareTo(BigDecimal.ZERO) > 0) {
			    cota.setVendaMedia(somaVendaMediaEquiv.divide(qtde, 2, BigDecimal.ROUND_HALF_UP).multiply(cota.getIndiceAjusteEquivalente()));
			}
		    }
		}
	    }
	}
    }
}
