package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - {@link MinimoMaximo}
 *      - {@link GravarReparteJuramentado}
 *      - {@link AjusteFinalReparte}
 *      - {@link ReparteComplementarPorCota}
 *      - {@link GravarReparteFinalCota}
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link DefinicaoBases}
 * Próximo Processo: N/A</p>
 */
public class CalcularReparte extends ProcessoAbstrato {

    @Override
    public void executarProcesso() throws Exception {
        executar();
        new MinimoMaximo().executar(estudo);
        new GravarReparteJuramentado().executar(estudo);
        new AjusteFinalReparte().executar(estudo);
        new ReparteComplementarPorCota().executar(estudo);
        new GravarReparteFinalCota().executar(estudo);
    }

    @Override
    public void executar() {
        // TODO: implementar método calcular do Processo CalcularReparte
    }
}
