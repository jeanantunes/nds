package br.com.abril.nds.process;

/**
 * Este processo apenas realiza um ajuste no reparte das cotas se a op��o "Venda M�dia + n" estiver marcada na tela de Ajuste de Reparte
 * 
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link VendaMediaFinal}
 * Pr�ximo Processo: {@link RedutorAutomatico}</p>
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
