package br.com.abril.nds.service.builders;

import br.com.abril.nds.model.cadastro.EnderecoTransportador;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalEndereco;

public class NotaFiscalTransportadorBuilder {
	
	public static NotaFiscal montarTransportador(NotaFiscal notaFiscal, NaturezaOperacao naturezaOperacao, Transportador transportador){
		
		NaturezaOperacao naOperacao = new NaturezaOperacao();
		
		naOperacao.setId(naturezaOperacao.getId());
		naOperacao.setCfopEstado(naturezaOperacao.getCfopEstado());
		naOperacao.setCfopOutrosEstados(naturezaOperacao.getCfopOutrosEstados());
		naOperacao.setDescricao(naturezaOperacao.getDescricao());
		// notaFiscal.setNaturezaOperacao(naOperacao);
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().setEndereco(new NotaFiscalEndereco());
		
		for (EnderecoTransportador enderecoTransportador : transportador.getEnderecosTransportador()){
			if(enderecoTransportador.isPrincipal()){
				notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setBairro(enderecoTransportador.getEndereco().getBairro());
				notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCep(enderecoTransportador.getEndereco().getCep());
				notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setNumero(enderecoTransportador.getEndereco().getNumero());
				notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCodigoCidadeIBGE(Long.valueOf(enderecoTransportador.getEndereco().getCodigoCidadeIBGE()));
				notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCidade(enderecoTransportador.getEndereco().getCidade());
				notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCodigoPais(0L);
				notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCodigoUf(Long.valueOf(enderecoTransportador.getEndereco().getCodigoUf()));
				notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setComplemento(enderecoTransportador.getEndereco().getComplemento());
				notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setPais("Brasil");
				notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setTipoLogradouro(enderecoTransportador.getEndereco().getTipoLogradouro());
				notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setUf(enderecoTransportador.getEndereco().getUf());
				break;
			}
		}
		
		
		return notaFiscal;
	}
	
}
