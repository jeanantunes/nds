package br.com.abril.nds.process;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - {@link CorrecaoVendas}
 * 
 * Processo Anterior: {@link CorrecaoTendencia}
 * Próximo Processo: N/A</p>
 */
public class VendaCrescente extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
        executar();
    }

    @Override
    protected void executar() {
        // TODO: implementar método calcular do SubProcesso VendaCrescente
    }
    
}
