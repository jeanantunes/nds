package br.com.abril.nds.process.bonificacoes;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustecota.AjusteCota;
import br.com.abril.nds.process.medias.Medias;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link Medias} Próximo Processo: {@link AjusteCota}
 * </p>
 */
@Component
public class Bonificacoes extends ProcessoAbstrato {

    private static final BigDecimal BIGDECIMAL_100 = BigDecimal.valueOf(100);

	@Override
    protected void executarProcesso() {
    	
    	for(Cota cota : getEstudo().getCotas()) {
    		if (getEstudo().getPercentualBonificacao() != null) {
    			BigDecimal percentualBonificacao = BigDecimal.ONE.add(getEstudo().getPercentualBonificacao()).divide(BIGDECIMAL_100);
    			if (percentualBonificacao.compareTo(cota.getIndiceTratamentoRegional()) > 0) {
    				cota.setIndiceTratamentoRegional(percentualBonificacao);
    			}
    			cota.setClassificacao(ClassificacaoCota.BonificacaoParaCotas);
    		}
    	}
    }
    

}
