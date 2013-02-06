package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o c�lculo da divis�o do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em considera��o todas as vari�veis tamb�m definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - {@link SelecaoBancas}
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link SomarFixacoes}
 * Pr�ximo Processo: {@link MontaTabelaEstudos}</p>
 */
public class VerificarTotalFixacoes extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
        executar();
        new SelecaoBancas().executar(estudo);
    }

    @Override
    protected void executar() {
        // TODO: implementar m�todo calcular do Processo VerificarTotalFixacoes
    }
    
}
