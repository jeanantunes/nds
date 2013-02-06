package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - {@link SelecaoBancas}
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link SomarFixacoes}
 * Próximo Processo: {@link MontaTabelaEstudos}</p>
 */
public class VerificarTotalFixacoes extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
        executar();
        new SelecaoBancas().executar(estudo);
    }

    @Override
    protected void executar() {
        // TODO: implementar método calcular do Processo VerificarTotalFixacoes
    }
    
}
