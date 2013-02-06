package br.com.abril.nds.process.encalhemaximo;

import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.complementarautomatico.ComplementarAutomatico;
import br.com.abril.nds.process.reparteproporcional.ReparteProporcional;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link ReparteProporcional} Próximo Processo:
 * {@link ComplementarAutomatico}
 * </p>
 */
public class EncalheMaximo extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
    }

}
