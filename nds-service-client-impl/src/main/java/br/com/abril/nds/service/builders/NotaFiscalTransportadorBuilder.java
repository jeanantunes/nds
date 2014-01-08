package br.com.abril.nds.service.builders;

import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;

public class NotaFiscalTransportadorBuilder {
	
	public static NotaFiscalNds montarTransportador(NotaFiscalNds notaFiscal, NaturezaOperacao naturezaOperacao){
		
		NaturezaOperacao naOperacao = new NaturezaOperacao();
		
		naOperacao.setId(naturezaOperacao.getId());
		naOperacao.setCfopEstado(naturezaOperacao.getCfopEstado());
		naOperacao.setCfopOutrosEstados(naturezaOperacao.getCfopOutrosEstados());
		naOperacao.setDescricao(naturezaOperacao.getDescricao());
		// notaFiscal.setNaturezaOperacao(naOperacao);
		
		return notaFiscal;
	}
	
}
