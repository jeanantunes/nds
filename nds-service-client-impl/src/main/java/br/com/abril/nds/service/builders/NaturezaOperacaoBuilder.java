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
		
		String cfop = "";
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().getPais()
				.equals(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().getPais())) {
			
			if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().getUf()
					.equals(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().getUf())) {
				cfop = naturezaOperacao.getCfopEstado();
			} else {
				cfop = naturezaOperacao.getCfopOutrosEstados();
			}
			
		} else {
			
			cfop = naturezaOperacao.getCfopExterior();
		}
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setDescricaoNaturezaOperacao((naturezaOperacao.getDescricao()).substring(0, 59));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setNumeroDocumentoFiscal(naturezaOperacao.getNotaFiscalNumeroNF());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setCodigoNF(StringUtils.leftPad(naturezaOperacao.getNotaFiscalNumeroNF().toString(), 8, '0'));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setSerie(naturezaOperacao.getNotaFiscalSerie());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setNaturezaOperacao(naturezaOperacao);
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setTipoOperacao(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getTipoOperacao());
		
	}
	
}