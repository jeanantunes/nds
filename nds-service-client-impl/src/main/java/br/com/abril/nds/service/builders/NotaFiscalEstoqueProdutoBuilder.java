package br.com.abril.nds.service.builders;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.CNPJDestinatario;
import br.com.abril.nds.model.fiscal.nota.CNPJEmitente;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormaPagamento;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalEndereco;

public class NotaFiscalEstoqueProdutoBuilder implements Serializable {
	
	private static final long serialVersionUID = 176874569807919538L;

	public static void popularDadosDistribuidor(br.com.abril.nds.model.fiscal.nota.NotaFiscal notaFiscal2, Distribuidor distribuidor, FiltroNFeDTO filtro) {
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente() == null) {
			notaFiscal2.getNotaFiscalInformacoes().setIdentificacaoEmitente(new br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente());
		}
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco() == null) {
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().setEndereco(new NotaFiscalEndereco());
		}
		
		// Dados do Distribuidor
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().setNome(distribuidor.getJuridica().getRazaoSocial().trim());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().setNomeFantasia(distribuidor.getJuridica().getNomeFantasia());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().setInscricaoEstadual(distribuidor.getJuridica().getInscricaoEstadual());
		CNPJEmitente cnpj = new CNPJEmitente();
		cnpj.setDocumento(distribuidor.getJuridica().getCnpj().replaceAll("/", "").replaceAll("\\.", "").replaceAll("-", ""));
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().setDocumento(cnpj);
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setId(distribuidor.getEnderecoDistribuidor().getEndereco().getId());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setUf(distribuidor.getEnderecoDistribuidor().getEndereco().getUf());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCidade(distribuidor.getEnderecoDistribuidor().getEndereco().getCidade());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setBairro(distribuidor.getEnderecoDistribuidor().getEndereco().getBairro());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setLogradouro(distribuidor.getEnderecoDistribuidor().getEndereco().getLogradouro());
	}
	
	public static void montarHeaderNotaFiscal(br.com.abril.nds.model.fiscal.nota.NotaFiscal notaFiscal2, EstoqueProduto estoqueProduto, Distribuidor distribuidor) {
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacao() == null) {
			notaFiscal2.getNotaFiscalInformacoes().setIdentificacao(new Identificacao());
		}
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario() == null) {
			notaFiscal2.getNotaFiscalInformacoes().setIdentificacaoDestinatario(new IdentificacaoDestinatario());
		}

		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setFormaPagamento(FormaPagamento.A_VISTA);
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setNumeroDocumentoFiscal(1L);
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setDataEmissao(new Date());
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setTipoOperacao(TipoOperacao.SAIDA);
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao();
		
		CNPJDestinatario cnpj = new CNPJDestinatario();
		cnpj.setDocumento(distribuidor.getJuridica().getCnpj().replaceAll("/", "").replaceAll("\\.", "").replaceAll("-", ""));
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setDocumento(cnpj);
	}
	
}
