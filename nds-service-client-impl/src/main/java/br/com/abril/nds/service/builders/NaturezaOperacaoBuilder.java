package br.com.abril.nds.service.builders;

import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

public class NaturezaOperacaoBuilder {
	
	public static void montarNaturezaOperacao(NotaFiscal notaFiscal, NaturezaOperacao naturezaOperacao) {
		
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacao() == null) {
			notaFiscal.getNotaFiscalInformacoes().setIdentificacao(new Identificacao());
		}
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setDescricaoNaturezaOperacao(naturezaOperacao.getDescricao());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setNumeroDocumentoFiscal(naturezaOperacao.getNotaFiscalNumeroNF());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setCodigoNF(naturezaOperacao.getNotaFiscalNumeroNF().toString());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setSerie(naturezaOperacao.getNotaFiscalSerie());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setNaturezaOperacao(naturezaOperacao);
		
	}
	
}