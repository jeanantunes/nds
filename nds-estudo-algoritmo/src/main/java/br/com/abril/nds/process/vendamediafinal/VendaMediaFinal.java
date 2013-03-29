package br.com.abril.nds.process.vendamediafinal;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.process.ajustereparte.AjusteReparte;
import br.com.abril.nds.process.jornaleirosnovos.JornaleirosNovos;

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

	private BigDecimal value = BigDecimal.ZERO;

	public void executar(CotaEstudo cota) {

		BigDecimal vendaMedia = cota.getVendaMedia();
		BigDecimal indiceAjusteCota = cota.getIndiceAjusteCota();
		BigDecimal indiceVendaCrescente = cota.getIndiceVendaCrescente();
		BigDecimal indiceTratamentoReginal = cota.getIndiceTratamentoRegional();
// FIXME INDICE DE TRATAMENTO REGIONAL DEVE INICIAR COM 1
		if (vendaMedia != null && indiceAjusteCota != null && indiceVendaCrescente != null && indiceTratamentoReginal != null) {
			value = cota.getVendaMedia().multiply(cota.getIndiceAjusteCota()).multiply(cota.getIndiceVendaCrescente()).multiply(cota.getIndiceTratamentoRegional());
		}
	}

	public BigDecimal getValue() {
		return value;
	}

}
