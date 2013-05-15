package br.com.abril.nds.process.ajustereparte;

import java.math.BigInteger;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.redutorautomatico.RedutorAutomatico;
import br.com.abril.nds.process.vendamediafinal.VendaMediaFinal;

/**
 * Este processo apenas realiza um ajuste no reparte das cotas se a opção
 * "Venda Média + n" estiver marcada na tela de Ajuste de Reparte. Se estiver,
 * ele atribui ao ReparteCalculado da cota a soma da VendaMediaFinal ou o valor
 * informado na tela Ajuste de Reparte (se ele for maior que o Pacote Padrão
 * definido, caso contrário será atribuído o pacote padrão).
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 	- N/A
 * Processo Pai:
 * 	- N/A
 * 
 * Processo Anterior: {@link VendaMediaFinal}
 * Próximo Processo: {@link RedutorAutomatico}
 * </p>
 */
@Component
public class AjusteReparte extends ProcessoAbstrato {

    @Override
    public void executar(EstudoTransient estudo) throws Exception {
	if ((estudo == null) || (estudo.getCotas() == null)) {
	    throw new Exception("Houve um erro durante a execução do processo Ajuste de Reparte. Erro: objeto Estudo nulo.");
	}
	for (CotaEstudo cota : estudo.getCotas()) {
	    if ((cota.getVendaMediaMaisN() != null) && (estudo.getPacotePadrao() != null) && (cota.getVendaMediaMaisN().compareTo(BigInteger.ZERO) > 0)) {
		BigInteger ajusteReparte = BigInteger.ZERO;
		if (cota.getVendaMediaMaisN().compareTo(estudo.getPacotePadrao()) > 0) {
		    ajusteReparte = cota.getVendaMediaMaisN();
		} else {
		    ajusteReparte = estudo.getPacotePadrao();
		}
		cota.setReparteCalculado(ajusteReparte.add(cota.getVendaMedia().toBigInteger()), estudo);
		cota.setClassificacao(ClassificacaoCota.ReparteFixado);
	    }
	}
    }
}
