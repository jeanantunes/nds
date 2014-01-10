package br.com.abril.nds.service.builders;

import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

public class NaturezaOperacaoBuilder {
	
	public static NotaFiscalNds montarNaturezaOperacao(NotaFiscalNds notaFiscal, NaturezaOperacao naturezaOperacao){
		
		NaturezaOperacao naOperacao = new NaturezaOperacao();
		
		naOperacao.setId(naturezaOperacao.getId());
		naOperacao.setCfopEstado(naturezaOperacao.getCfopEstado());
		naOperacao.setCfopOutrosEstados(naturezaOperacao.getCfopOutrosEstados());
		naOperacao.setDescricao(naturezaOperacao.getDescricao());
		// notaFiscal.setNaturezaOperacao(naOperacao);
		
		return notaFiscal;
	}

	public static void montarNaturezaOperacao(NotaFiscal notaFiscal2, NaturezaOperacao naturezaOperacao) {
		
		if(notaFiscal2.getIdentificacao() == null) {
			notaFiscal2.setIdentificacao(new Identificacao());
		}
		notaFiscal2.getIdentificacao().setDescricaoNaturezaOperacao(naturezaOperacao.getDescricao());
		
	}
	
}