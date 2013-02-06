package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o c�lculo da divis�o do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em considera��o todas as vari�veis tamb�m definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - {@link CorrecaoVendas}
 * 
 * Processo Anterior: N/A
 * Pr�ximo Processo: {@link CorrecaoTendencia}</p>
 */
public class CorrecaoIndividual extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
        executar();
    }

    @Override
    protected void executar() {
        // TODO: implementar m�todo calcular do SunProcesso CorrecaoIndividual
    }
    
}
