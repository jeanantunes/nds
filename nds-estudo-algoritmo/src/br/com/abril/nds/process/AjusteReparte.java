package br.com.abril.nds.process;

import java.math.BigDecimal;

import br.com.abril.nds.model.Cota;

/**
 * Este processo apenas realiza um ajuste no reparte das cotas se a opção "Venda Média + n" estiver marcada na tela de Ajuste de Reparte
 * Se estiver, ele atribui ao ReparteCalculado da cota a soma da VendaMediaFinal ao valor informado na tela Ajuste de Reparte (se ele for
 * menor que o Pacote Padrão definido, será somado o Pacote Padrão ao invés desse valor). 
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
        executar();
    }

    @Override
    protected void executar() {
    	for (Cota cota : estudo.getParametro().getCotas()) {
    		if (cota.isVendaMediaMaisN()) {
    			BigDecimal ajusteReparte = new BigDecimal(0);
    			if (cota.getAjusteReparte().intValue() > estudo.getParametro().getPacotePadrao().intValue()) {
    				ajusteReparte = cota.getAjusteReparte();
    			} else {
    				ajusteReparte = estudo.getParametro().getPacotePadrao();
    			}
    			cota.setReparteCalculado(cota.getVendaMediaFinal().add(ajusteReparte));
    		}	
    	}
    } 
}
