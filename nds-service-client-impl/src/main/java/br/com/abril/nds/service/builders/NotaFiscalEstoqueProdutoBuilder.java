package br.com.abril.nds.service.builders;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroViewNotaFiscalDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormaPagamento;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;

public class NotaFiscalEstoqueProdutoBuilder implements Serializable {
	
	private static final long serialVersionUID = 176874569807919538L;
	
	// builder header Nota fiscal
	public static NotaFiscalNds montarHeaderNotaFiscal(NotaFiscalNds notaFiscal, EstoqueProduto estoqueProduto){
		notaFiscal.getEmissor().setRazaoSocial("pessoa Juridica");
		notaFiscal.getEmissor().setNomeFantasia("Teste teste teste teste");
		notaFiscal.getEmissor().setInscricaoEstadual("pessoaJuridica.getInscricaoEstadual()");
		notaFiscal.getEmissor().setCnpj("301852668001160");
		notaFiscal.getEmissor().setEmail("teste@teste.com");
		return notaFiscal;
	}


	// metodo responsavel pelo dados do distribuidor da nota
	public static NotaFiscalNds popularDadosDistribuidor(NotaFiscalNds notaFiscal, Distribuidor distribuidor, FiltroViewNotaFiscalDTO filtro){
		
		// Dados do Distribuidor
		notaFiscal.getEmissor().setNomeFantasia(distribuidor.getJuridica().getRazaoSocial());
		notaFiscal.getEmissor().setInscricaoEstadual(distribuidor.getJuridica().getInscricaoEstadual());
		notaFiscal.getEmissor().setCnpj(distribuidor.getJuridica().getCnpj());
		
		// Endereço
		notaFiscal.getEmissor().getNotaFicalEndereco().setUf(distribuidor.getEnderecoDistribuidor().getEndereco().getUf());
		notaFiscal.getEmissor().getNotaFicalEndereco().setCidade(distribuidor.getEnderecoDistribuidor().getEndereco().getCidade());
		notaFiscal.getEmissor().getNotaFicalEndereco().setBairro(distribuidor.getEnderecoDistribuidor().getEndereco().getBairro());
		notaFiscal.getEmissor().getNotaFicalEndereco().setLogradouro(distribuidor.getEnderecoDistribuidor().getEndereco().getLogradouro());
		
		return notaFiscal;
	}

	public static void popularDadosDistribuidor(br.com.abril.nds.model.fiscal.nota.NotaFiscal notaFiscal2, Distribuidor distribuidor, FiltroViewNotaFiscalDTO filtro) {
		
		if(notaFiscal2.getIdentificacaoEmitente() == null) {
			notaFiscal2.setIdentificacaoEmitente(new br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente());
		}
		
		if(notaFiscal2.getIdentificacaoEmitente().getEndereco() == null) {
			notaFiscal2.getIdentificacaoEmitente().setEndereco(new Endereco());
		}
		
		// Dados do Distribuidor
		notaFiscal2.getIdentificacaoEmitente().setNome(distribuidor.getJuridica().getRazaoSocial());
		notaFiscal2.getIdentificacaoEmitente().setNomeFantasia(distribuidor.getJuridica().getNomeFantasia());
		notaFiscal2.getIdentificacaoEmitente().setInscricaoEstadual(distribuidor.getJuridica().getInscricaoEstadual());
		notaFiscal2.getIdentificacaoEmitente().setDocumento("1233");
		
		notaFiscal2.getIdentificacaoEmitente().getEndereco().setId(distribuidor.getEnderecoDistribuidor().getEndereco().getId());
		notaFiscal2.getIdentificacaoEmitente().getEndereco().setUf(distribuidor.getEnderecoDistribuidor().getEndereco().getUf());
		notaFiscal2.getIdentificacaoEmitente().getEndereco().setCidade(distribuidor.getEnderecoDistribuidor().getEndereco().getCidade());
		notaFiscal2.getIdentificacaoEmitente().getEndereco().setBairro(distribuidor.getEnderecoDistribuidor().getEndereco().getBairro());
		notaFiscal2.getIdentificacaoEmitente().getEndereco().setLogradouro(distribuidor.getEnderecoDistribuidor().getEndereco().getLogradouro());
	}
	
	public static void montarHeaderNotaFiscal(br.com.abril.nds.model.fiscal.nota.NotaFiscal notaFiscal2, EstoqueProduto estoqueProduto) {
		
		if(notaFiscal2.getIdentificacao() == null) {
			notaFiscal2.setIdentificacao(new Identificacao());
		}
		
		if(notaFiscal2.getIdentificacaoDestinatario() == null) {
			notaFiscal2.setIdentificacaoDestinatario(new IdentificacaoDestinatario());
		}

		notaFiscal2.getIdentificacao().setFormaPagamento(FormaPagamento.A_VISTA);
		
		notaFiscal2.getIdentificacao().setNumeroDocumentoFiscal(1L);
		
		notaFiscal2.getIdentificacao().setDataEmissao(new Date());
		
		notaFiscal2.getIdentificacao().setSerie(1);
		
		notaFiscal2.getIdentificacao().setTipoOperacao(TipoOperacao.SAIDA);
		
		notaFiscal2.getIdentificacao();
		
		notaFiscal2.getIdentificacaoDestinatario().setDocumento("123");
	}
	
}
