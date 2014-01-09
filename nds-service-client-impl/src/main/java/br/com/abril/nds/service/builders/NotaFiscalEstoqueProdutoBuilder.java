package br.com.abril.nds.service.builders;

import java.io.Serializable;

import br.com.abril.nds.dto.filtro.FiltroViewNotaFiscalDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;

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
	
	public TipoOperacao tipoOperacaoNotaFiscal (){
		
		return null;
	}
}
