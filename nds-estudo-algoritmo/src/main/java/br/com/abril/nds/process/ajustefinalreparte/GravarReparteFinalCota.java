package br.com.abril.nds.process.ajustefinalreparte;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;
import br.com.abril.nds.service.EstudoAlgoritmoService;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o
 * perfil definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 	- N/A
 * Processo Pai:
 * 	- {@link CalcularReparte}
 * 
 * Processo Anterior: {@link ReparteComplementarPorCota}
 * Próximo Processo: N/A
 * </p>
 */
@Component
public class GravarReparteFinalCota extends ProcessoAbstrato {

    @Autowired
    private EstudoAlgoritmoService estudoAlgoritmoService;

    @Override
    public void executar(EstudoTransient estudo) {
	
    estudo.getCotas().addAll(estudo.getCotasExcluidas());
	estudo.getCotas().addAll(estudo.getCotasForaDaRegiao());
	estudo.getCotas().addAll(estudo.getCotasSoComEdicaoAberta());
	estudo.getCotas().addAll(estudo.getCotasComReparteJaCalculado());
	
	for (CotaEstudo cota : estudo.getCotas()) {
	    if (cota.getClassificacao().notIn(ClassificacaoCota.RedutorAutomatico, ClassificacaoCota.CotaNova) 
	    		&& cota.getReparteCalculado() != null && cota.getReparteCalculado().compareTo(BigInteger.ZERO) == 0) {
	    	cota.setReparteCalculado(null);
	    }
	    
	    if(cota.getClassificacao().in(ClassificacaoCota.CotaMixSemMinMax)){
	    	cota.setClassificacao(ClassificacaoCota.CotaMix);
	    }
	}
	
	estudo.setDataLancamento(estudo.getProdutoEdicaoEstudo().getDataLancamento());
	
	estudoAlgoritmoService.gravarEstudo(estudo);
    }
}
