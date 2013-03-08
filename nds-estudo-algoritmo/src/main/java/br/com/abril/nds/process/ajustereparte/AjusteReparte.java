package br.com.abril.nds.process.ajustereparte;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
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
    protected void executarProcesso() throws Exception {
	// TODO: ainda resta efetuar a consulta dos parâmetros que alimentam o método
	if ((getEstudo() == null) || (getEstudo().getCotas() == null)) {
	    throw new Exception("Houve um erro durante a execução do processo Ajuste de Reparte. Erro: objeto Estudo nulo.");
	}
	for (Cota cota : getEstudo().getCotas()) {
	    if ((cota.getVendaMediaMaisN() != null) && (getEstudo().getPacotePadrao() != null) && (cota.getVendaMediaMaisN().longValue() > 0)) {
		BigDecimal ajusteReparte = BigDecimal.ZERO;
		if (cota.getVendaMediaMaisN().longValue() > getEstudo().getPacotePadrao().longValue()) {
		    ajusteReparte = cota.getVendaMediaMaisN();
		} else {
		    ajusteReparte = getEstudo().getPacotePadrao();
		}
		cota.setReparteCalculado(cota.getVendaMedia().add(ajusteReparte));
		cota.setClassificacao(ClassificacaoCota.ReparteFixado);
	    }
	}
    }
}
