package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o c�lculo da divis�o do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em considera��o todas as vari�veis tamb�m definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - {@link VerificarTotalFixacoes}
 * 
 * Processo Anterior: N/A
 * Pr�ximo Processo: N/A</p>
 */
public class SelecaoBancas extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
        executar();
    }

    @Override
    protected void executar() {
        // TODO: implementar m�todo calcular do SubProcesso SelecaoBancas
    }
    
}
