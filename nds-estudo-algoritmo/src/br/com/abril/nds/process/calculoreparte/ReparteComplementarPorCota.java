package br.com.abril.nds.process.calculoreparte;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CalcularReparte}
 * 
 * Processo Anterior: {@link AjusteFinalReparte} Próximo Processo:
 * {@link GravarReparteFinalCota}
 * </p>
 */
public class ReparteComplementarPorCota extends ProcessoAbstrato {

    public ReparteComplementarPorCota(Estudo estudo) {
	super(estudo);
    }

    @Override
    protected void executarProcesso() {
    }

}
