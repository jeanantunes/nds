package br.com.abril.nds.process.vendamediafinal;

import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustereparte.AjusteReparte;
import br.com.abril.nds.process.jornaleirosnovos.JornaleirosNovos;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link JornaleirosNovos} Próximo Processo:
 * {@link AjusteReparte}
 * </p>
 */
public class VendaMediaFinal extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
    }

}
