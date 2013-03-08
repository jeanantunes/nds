package br.com.abril.nds.process.calculoreparte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.service.EstudoServiceEstudo;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o
 * perfil definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 	- N/A
 * Processo Pai:
 * 	- {@link CalcularReparte}
 * 
 * Processo Anterior: {@link ReparteComplementarPorCota}
 * Próximo Processo: N/A
 * </p>
 */
@Component
public class GravarReparteFinalCota extends ProcessoAbstrato {

    @Autowired
    private EstudoServiceEstudo estudoServiceEstudo;

    @Override
    protected void executarProcesso() {
	estudoServiceEstudo.gravarEstudo(getEstudo());
    }
}
