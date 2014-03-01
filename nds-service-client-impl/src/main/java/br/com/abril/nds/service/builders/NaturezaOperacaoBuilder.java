package br.com.abril.nds.service.builders;

import org.apache.commons.lang.StringUtils;

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
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setCodigoNF(StringUtils.leftPad(naturezaOperacao.getNotaFiscalNumeroNF().toString(), 8, '0'));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setSerie(naturezaOperacao.getNotaFiscalSerie());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setNaturezaOperacao(naturezaOperacao);
		
	}
	
}