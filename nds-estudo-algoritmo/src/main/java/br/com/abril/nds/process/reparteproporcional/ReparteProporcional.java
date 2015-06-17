package br.com.abril.nds.process.reparteproporcional;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.encalhemaximo.EncalheMaximo;
import br.com.abril.nds.process.reparteminimo.ReparteMinimo;
import br.com.abril.nds.service.EstudoAlgoritmoService;

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
		
    	BigDecimal indiceReparteEdicoesAbertas = BigDecimal.ZERO;
		
    	if(temEdicaoBaseAberta && estudo.getEdicoesBase().size() > 1){
    		
    		if(estudo.getSomatoriaReparteEdicoesAbertas().compareTo(BigDecimal.ZERO) > 0){
    			
    			// ÍndiceRepAberta =  RepDistribInicial / ΣRepEdiçãoAberta
    		    // só entrar nesse calculo se houver mais de uma edicao base
    		    // somatoria somente deve conter reparte da edicao aberta de todas as cotas que só receberam uma edicao aberta
    		    // validação ocorre em {@link EstudoService.calculate}
    		    
        		indiceReparteEdicoesAbertas = new BigDecimal(estudo.getReparteDistribuirInicial()).divide(estudo.getSomatoriaReparteEdicoesAbertas(), 3, BigDecimal.ROUND_HALF_UP);
    			
    		}
    		
    		for (CotaEstudo cota : estudo.getCotas()) {
    			
    			if (cota.isCotaSoRecebeuEdicaoAberta() && cota.getSomaReparteEdicoesAbertas().compareTo(BigDecimal.ZERO) > 0 &&
				cota.getClassificacao().notIn(ClassificacaoCota.CotaMix, ClassificacaoCota.ReparteFixado)) {
    				
    				// RepCalculadoCota = ARRED(RepEdicaoAbertaCota * IndiceRepAberta; 0)
    			
    				BigDecimal repCalculado = cota.getSomaReparteEdicoesAbertas().multiply(indiceReparteEdicoesAbertas);
    				
    				cota.setReparteCalculado(EstudoAlgoritmoService.arredondarPacotePadrao(estudo, repCalculado), estudo);
    				cota.setClassificacao(ClassificacaoCota.BancaSoComEdicaoBaseAberta);
    				
    				estudo.getCotasSoComEdicaoAberta().add(cota);
    			}
    		}
    		
    		estudo.getCotas().removeAll(estudo.getCotasSoComEdicaoAberta());
    		
    	}
    }
}
