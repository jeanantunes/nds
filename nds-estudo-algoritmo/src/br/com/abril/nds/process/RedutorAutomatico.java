package br.com.abril.nds.process;

import java.math.BigDecimal;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;

/**
 * Processo que tem como objetivo efetuar o c�lculo da divis�o do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em considera��o todas as vari�veis tamb�m definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link AjusteReparte}
 * Pr�ximo Processo: {@link ReparteMinimo}</p>
 */
public class RedutorAutomatico extends ProcessoAbstrato {

    @Override
    protected void executarProcesso() {
        executar();
    }

    @Override
    protected void executar() {
    	
    	BigDecimal excedente = new BigDecimal(0);
    	
    	for (Cota cota : estudo.getCotas()) {
    		if (cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
    				|| cota.getClassificacao().equals(ClassificacaoCota.BancaSoComEdicaoBaseAberta)
    				|| cota.getClassificacao().equals(ClassificacaoCota.RedutorAutomatico))
    		estudo.setReparteDistribuir(estudo.getReparteDistribuir().subtract(cota.getReparteCalculado()));
    	}
    }
    
}
