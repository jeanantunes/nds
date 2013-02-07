package br.com.abril.nds.process.complementarautomatico;

import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;
import br.com.abril.nds.process.encalhemaximo.EncalheMaximo;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link EncalheMaximo} Próximo Processo:
 * {@link CalcularReparte}
 * </p>
 */
public class ComplementarAutomatico extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
    }

}
