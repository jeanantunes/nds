package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o c�lculo da divis�o do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em considera��o todas as vari�veis tamb�m definidas no setup.
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
 * Pr�ximo Processo: N/A</p>
 */
public class CalcularReparte extends ProcessoAbstrato {

    @Override
    public void executarProcesso() {
        calcular();
//        new MinimoMaximo().executar(estudo);
//        new GravarReparteJuramentado().executar(estudo);
//        new AjusteFinalReparte().executar(estudo);
//        new ReparteComplementarPorCota().executar(estudo);
//        new GravarReparteFinalCota().executar(estudo);
    }

    @Override
    public void calcular() {
        // TODO: implementar m�todo calcular do Processo CalcularReparte
    }
}
