package br.com.abril.nds.service.builders;

import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

public class NotaFiscalTransportadorBuilder {
	
	public static NotaFiscal montarTransportador(NotaFiscal notaFiscal, NaturezaOperacao naturezaOperacao){
		
		NaturezaOperacao naOperacao = new NaturezaOperacao();
		
		naOperacao.setId(naturezaOperacao.getId());
		naOperacao.setCfopEstado(naturezaOperacao.getCfopEstado());
		naOperacao.setCfopOutrosEstados(naturezaOperacao.getCfopOutrosEstados());
		naOperacao.setDescricao(naturezaOperacao.getDescricao());
		// notaFiscal.setNaturezaOperacao(naOperacao);
		
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setBairro("Osasco");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCep("08250000");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setNumero("158");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCodigoCidadeIBGE(3550308L);
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCidade("Sãp Paulo");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCodigoPais(0L);
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCodigoUf(0L);
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setComplemento("XXXX");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setPais("Brasil");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setTipoLogradouro("Rua");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setUf("SP");
		return notaFiscal;
	}
	
}
