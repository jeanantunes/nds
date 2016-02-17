package br.com.abril.nds.process.vendamediafinal;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ajustereparte.AjusteReparte;
import br.com.abril.nds.process.jornaleirosnovos.JornaleirosNovos;
import br.com.abril.nds.util.BigDecimalUtil;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link JornaleirosNovos} Próximo Processo: {@link AjusteReparte} </p>
 */
@Component
public class VendaMediaFinal {

    public void executar(EstudoTransient estudo) {

	for (CotaEstudo cota : estudo.getCotas()) {
	    if (cota.getVendaMedia() != null) {
	    	
			if (cota.getIndiceAjusteCota() != null) {
			    cota.setVendaMedia(cota.getVendaMedia().multiply(cota.getIndiceAjusteCota()));
			}
			if (cota.getIndiceVendaCrescente() != null) {
			    cota.setVendaMedia(cota.getVendaMedia().multiply(cota.getIndiceVendaCrescente()));
			}
			if (cota.getIndiceCorrecaoTendencia() != null) {
			    cota.setVendaMedia(cota.getVendaMedia().multiply(cota.getIndiceCorrecaoTendencia()));
			}
			if (cota.getIndiceTratamentoRegional() != null) {
			    cota.setVendaMedia(cota.getVendaMedia().multiply(cota.getIndiceTratamentoRegional()));
			}
			if (BigDecimalUtil.isMenorQueZero(cota.getVendaMedia())) {
				cota.setVendaMedia(BigDecimal.ZERO);
			}
	    }
	}
    }
}
