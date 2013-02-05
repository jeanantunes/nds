package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o c�lculo da divis�o do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em considera��o todas as vari�veis tamb�m definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - {@link CalcularReparte}
 * 
 * Processo Anterior: N/A
 * Pr�ximo Processo: {@link GravarReparteJuramentado}</p>
 */
public class MinimoMaximo extends ProcessoAbstrato {
    
    @Override
    public void executarProcesso() {
        calcular();
    }

    @Override
    protected void calcular() {
        // TODO: implementar m�todo calcular do SubProcesso MinimoMaximo
    }
}
