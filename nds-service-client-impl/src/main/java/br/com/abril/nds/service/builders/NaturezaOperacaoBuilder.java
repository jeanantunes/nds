package br.com.abril.nds.service.builders;

import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nfe.model.NotaFiscal;

public class NaturezaOperacaoBuilder {
	
	public static NotaFiscal montarNaturezaOperacao(NotaFiscal notaFiscal, NaturezaOperacao naturezaOperacao){
		
		br.com.abril.nfe.model.NaturezaOperacao naOperacao = new br.com.abril.nfe.model.NaturezaOperacao();
		
		naOperacao.setId(naturezaOperacao.getId());
		naOperacao.setCfopEstado(naturezaOperacao.getCfopEstado());
		naOperacao.setCfopForaEstado(naturezaOperacao.getCfopOutrosEstados());
		naOperacao.setDescricao(naturezaOperacao.getDescricao());
		notaFiscal.setNaturezaOperacao(naOperacao);
		
		return notaFiscal;
	}
	
}
