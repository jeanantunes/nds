package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o c�lculo da divis�o do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em considera��o todas as vari�veis tamb�m definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - {@link CalcularReparte}
 * 
 * Processo Anterior: {@link MinimoMaximo}
 * Pr�ximo Processo: {@link AjusteFinalReparte}</p>
 */
public class GravarReparteJuramentado extends ProcessoAbstrato {

    @Override
    public void executarProcesso() {
        calcular();
    }

    @Override
    protected void calcular() {
        // TODO: implementar m�todo calcular do SubProcesso GravarReparteJuramentado
    }
}
