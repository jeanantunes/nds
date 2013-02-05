package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o c�lculo da divis�o do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em considera��o todas as vari�veis tamb�m definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link Bonificacoes}
 * Pr�ximo Processo: {@link JornaleirosNovos}</p>
 */
public class AjusteCota extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
        calcular();
    }

    @Override
    protected void calcular() {
        // TODO: implementar m�todo calcular do Processo AjusteCota
    }
    
}
