package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link AjusteReparte}
 * Próximo Processo: {@link ReparteMinimo}</p>
 */
public class RedutorAutomatico extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
        calcular();
    }

    @Override
    protected void calcular() {
        // TODO: implementar método calcular do Processo RedutorAutomatico
    }
    
}
