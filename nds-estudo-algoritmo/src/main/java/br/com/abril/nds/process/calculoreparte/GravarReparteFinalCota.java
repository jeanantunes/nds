package br.com.abril.nds.process.calculoreparte;

import java.math.BigDecimal;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CalcularReparte}
 * 
 * Processo Anterior: {@link ReparteComplementarPorCota} Próximo Processo: N/A
 * </p>
 */
public class GravarReparteFinalCota extends ProcessoAbstrato {

    public GravarReparteFinalCota(Estudo estudo) {
	super(estudo);
    }

    @Override
    protected void executarProcesso() {
    	

    	for(Cota cota:getEstudo().getCotas()){
    		//evitando referencia de objetos :p
    		cota.setReparteFinalCota(BigDecimal.ZERO.add(cota.getReparteCalculado()));
    	}
    }

}
