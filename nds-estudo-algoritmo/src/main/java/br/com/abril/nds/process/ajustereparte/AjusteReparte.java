package br.com.abril.nds.process.ajustereparte;

import java.math.BigDecimal;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.redutorautomatico.RedutorAutomatico;
import br.com.abril.nds.process.vendamediafinal.VendaMediaFinal;

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
	
	Estudo estudo = (Estudo) super.genericDTO;
	
    	// TODO: ainda resta efetuar a consulta dos parâmetros que alimentam o método
    	for (Cota cota : estudo.getCotas()) {
    		if (cota.getVendaMediaMaisN().longValue() > 0) {
    			BigDecimal ajusteReparte = new BigDecimal(0);
    			if (cota.getVendaMediaMaisN().longValue() > estudo.getPacotePadrao().longValue()) {
    				ajusteReparte = cota.getVendaMediaMaisN();
    			} else {
    				ajusteReparte = estudo.getPacotePadrao();
    			}
    			cota.setReparteCalculado(cota.getVendaMedia().add(ajusteReparte));
    		}	
    	}
    }
}
