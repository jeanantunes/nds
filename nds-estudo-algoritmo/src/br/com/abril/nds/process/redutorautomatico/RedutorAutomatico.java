package br.com.abril.nds.process.redutorautomatico;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustereparte.AjusteReparte;
import br.com.abril.nds.process.reparteminimo.ReparteMinimo;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link AjusteReparte} Próximo Processo:
 * {@link ReparteMinimo}
 * </p>
 */
public class RedutorAutomatico extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {

	// FIXME: concluir desenvolvimento do processo após a resposta do JTrac
	// BigDecimal excedente = new BigDecimal(0);

	for (Cota cota : estudo.getCotas()) {
	    if (cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
		    || cota.getClassificacao().equals(
			    ClassificacaoCota.BancaSoComEdicaoBaseAberta)
		    || cota.getClassificacao().equals(
			    ClassificacaoCota.RedutorAutomatico))
		estudo.setReparteDistribuir(estudo.getReparteDistribuir()
			.subtract(cota.getReparteCalculado()));
	}
    }

}
