package br.com.abril.nds.service.builders;

import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

public class NaturezaOperacaoBuilder {
	
	public static void montarNaturezaOperacao(NotaFiscal notaFiscal2, NaturezaOperacao naturezaOperacao) {
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacao() == null) {
			notaFiscal2.getNotaFiscalInformacoes().setIdentificacao(new Identificacao());
		}
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setDescricaoNaturezaOperacao(naturezaOperacao.getDescricao());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setSerie(naturezaOperacao.getNotaFiscalSerie());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setNaturezaOperacao(naturezaOperacao);
		
	}
	
}