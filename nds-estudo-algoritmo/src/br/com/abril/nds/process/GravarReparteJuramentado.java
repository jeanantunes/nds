package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - {@link CalcularReparte}
 * 
 * Processo Anterior: {@link MinimoMaximo}
 * Próximo Processo: {@link AjusteFinalReparte}</p>
 */
public class GravarReparteJuramentado extends ProcessoAbstrato {

    @Override
    public void executarProcesso() {
        executar();
    }

    @Override
    protected void executar() {
        // TODO: implementar método calcular do SubProcesso GravarReparteJuramentado
    }
}
