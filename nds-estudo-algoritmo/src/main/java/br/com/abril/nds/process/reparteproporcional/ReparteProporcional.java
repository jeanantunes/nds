package br.com.abril.nds.process.reparteproporcional;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
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
    public void executar(EstudoTransient estudo) {
    	boolean temEdicaoBaseAberta = false;
    	for (ProdutoEdicaoEstudo edicaoBase : estudo.getEdicoesBase()) {
    		if (edicaoBase.isEdicaoAberta()) {
    			temEdicaoBaseAberta = true;
    			break;
    		}
    	}
    	BigInteger somaReparteProporcional = BigInteger.ZERO;
    	BigDecimal indiceReparteEdicoesAbertas = BigDecimal.ZERO;
    	if (estudo.getSomatoriaReparteEdicoesAbertas().compareTo(BigDecimal.ZERO) > 0) {
    		// ÍndiceRepAberta =  RepDistribInicial / ΣRepEdiçãoAberta
    		indiceReparteEdicoesAbertas = new BigDecimal(estudo.getReparteDistribuirInicial()).divide(estudo.getSomatoriaReparteEdicoesAbertas(), 3, BigDecimal.ROUND_HALF_UP);
    	} 
    	for (CotaEstudo cota : estudo.getCotas()) {
    		if (temEdicaoBaseAberta && cota.isCotaSoRecebeuEdicaoAberta()) {
    			// RepCalculadoCota = ARRED(RepEdiçãoAbertaCota * ÍndiceRepAberta; 0)
    			BigDecimal repCalculado = cota.getSomaReparteEdicoesAbertas().multiply(indiceReparteEdicoesAbertas);
    			cota.setReparteCalculado(repCalculado.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_HALF_UP).toBigInteger());
    			
    			cota.setClassificacao(ClassificacaoCota.BancaSoComEdicaoBaseAberta);
    			somaReparteProporcional = somaReparteProporcional.add(cota.getReparteCalculado());
    		}
    	}
    	// RepDistribuir = RepDistribuir – ΣRepProporcional
    	estudo.setReparteDistribuir(estudo.getReparteDistribuir().subtract(somaReparteProporcional));
    }
}
