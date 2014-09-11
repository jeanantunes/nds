package br.com.abril.nds.process.verificartotalfixacoes;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - {@link SelecaoBancas} Processo Pai: - N/A
 * 
 * Processo Anterior: {@link SomarFixacoes} Próximo Processo: {@link MontaTabelaEstudos}
 * </p>
 */
@Component
public class VerificarTotalFixacoes extends ProcessoAbstrato {

//    @Autowired
//    private SelecaoBancas selecaoBancas;

    @Override
    public void executar(EstudoTransient estudo) throws Exception {
//	selecaoBancas.executar(estudo);

	if (estudo.isUsarFixacao()) {
	    BigInteger somaFixacao = BigInteger.ZERO;
	    for (CotaEstudo cota : estudo.getCotasExcluidas()) {
			if (cota.getReparteFixado() != null) {
			    somaFixacao = somaFixacao.add(cota.getReparteFixado());
			}
	    }
	    BigDecimal maximoFixacao = null;
	    if (estudo.getPercentualMaximoFixacao() != null) {
	    	maximoFixacao = new BigDecimal(estudo.getReparteDistribuir()).multiply(
	    			estudo.getPercentualMaximoFixacao().divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
	    }
	    if ((maximoFixacao != null) && (maximoFixacao.compareTo(new BigDecimal(somaFixacao)) < 0)) {
	    	throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "A soma das fixações de reparte é maior que o percentual configurado na tela de parâmetros do distribuidor."));
	    }
	}
	
	if(estudo.getCotas().isEmpty()){
		throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não há cotas aptas a receberem reparte."));	
	}
	
    }
}
