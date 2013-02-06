package br.com.abril.nds.process;

/**
 * Este processo apenas realiza um ajuste no reparte das cotas se a opção "Venda Média + n" estiver marcada na tela de Ajuste de Reparte
 * 
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link VendaMediaFinal}
 * Próximo Processo: {@link RedutorAutomatico}</p>
 */
public class AjusteReparte extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
        calcular();
    }

    @Override
    protected void calcular() {
//        if (estudo.getParametro().isVendaMediaMaisN()) {
//        	//estudo.
//        }
    }
    
}
