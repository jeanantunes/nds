package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - {@link CalcularReparte}
 * 
 * Processo Anterior: N/A
 * Próximo Processo: {@link GravarReparteJuramentado}</p>
 */
public class MinimoMaximo extends ProcessoAbstrato {
    
    @Override
    public void executarProcesso() {
        calcular();
    }

    @Override
    protected void calcular() {
        // TODO: implementar método calcular do SubProcesso MinimoMaximo
    }
}
