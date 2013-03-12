package br.com.abril.nds.process.reparteproporcional;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.encalhemaximo.EncalheMaximo;
import br.com.abril.nds.process.reparteminimo.ReparteMinimo;

/**
 * Processo que tem por objetivo fazer um reparte proporcional entre as cotas, se houver alguma edição aberta nas bases
 * e essa cota só recebeu edições abertas.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - N/A
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link ReparteMinimo}
 * Próximo Processo: {@link EncalheMaximo}</p>
 */
@Component
public class ReparteProporcional extends ProcessoAbstrato {
	
    @Override
    protected void executarProcesso() {
    	boolean temEdicaoBaseAberta = false;
    	for (ProdutoEdicaoBase edicaoBase : getEstudo().getEdicoesBase()) {
    		if (edicaoBase.isEdicaoAberta()) {
    			temEdicaoBaseAberta = true;
    			break;
    		}
    	}
    	BigDecimal somaReparteProporcional = BigDecimal.ZERO;
    	BigDecimal indiceReparteEdicoesAbertas = BigDecimal.ZERO;
    	if (getEstudo().getSomatoriaReparteEdicoesAbertas().compareTo(BigDecimal.ZERO) > 0) {
    		// ÍndiceRepAberta =  RepDistribInicial / ΣRepEdiçãoAberta
    		indiceReparteEdicoesAbertas = getEstudo().getReparteDistribuirInicial().divide(getEstudo().getSomatoriaReparteEdicoesAbertas(), 3, BigDecimal.ROUND_HALF_UP);
    	} 
    	for (Cota cota : getEstudo().getCotas()) {
    		if (temEdicaoBaseAberta && cota.isCotaSoRecebeuEdicaoAberta()) {
    			// RepCalculadoCota = ARRED(RepEdiçãoAbertaCota * ÍndiceRepAberta; 0)
    			BigDecimal repCalculado = cota.getSomaReparteEdicoesAbertas().multiply(indiceReparteEdicoesAbertas);
    			repCalculado = repCalculado.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_HALF_UP);
    			cota.setReparteCalculado(repCalculado);
    			
    			cota.setClassificacao(ClassificacaoCota.BancaSoComEdicaoBaseAberta);
    			somaReparteProporcional = somaReparteProporcional.add(cota.getReparteCalculado());
    		}
    	}
    	// RepDistribuir = RepDistribuir – ΣRepProporcional
    	getEstudo().setReparteDistribuir(getEstudo().getReparteDistribuir().subtract(somaReparteProporcional));
    }
}
