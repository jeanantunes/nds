package br.com.abril.nds.process.bonificacoes;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.EstudoTransient;
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

//    private static final BigDecimal BIGDECIMAL_100 = BigDecimal.valueOf(100);

	@Override
	public void executar(EstudoTransient estudo) {
    	
//    	if (estudo.getPercentualBonificacao() != null) { //TODO verificar regiao
//    		for(CotaEstudo cota : estudo.getCotas()) {
//    			//TODO verificar se cota pertence a regio informada no estudo
//    			BigDecimal percentualBonificacao = BigDecimal.ONE.add(estudo.getPercentualBonificacao()).divide(BIGDECIMAL_100);
//    			if (percentualBonificacao.compareTo(cota.getIndiceTratamentoRegional()) > 0) {
//    				cota.setIndiceTratamentoRegional(percentualBonificacao);
//    			}
//    			cota.setClassificacao(ClassificacaoCota.BonificacaoParaCotas);
//    		}
//    	}
    }
    

}
