package br.com.abril.nds.service.builders;

import java.util.Date;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFicalEndereco;;

public class EmitenteDestinatarioBuilder {
	
	public static NotaFiscalNds montarEnderecoEmitenteDestinatario (NotaFiscalNds notaFiscal, Cota cota){
		
		if (cota.getPessoa() instanceof PessoaJuridica) {
			PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
			notaFiscal.getEmitenteDestinario().setNome(pessoaJuridica.getRazaoSocial());
			notaFiscal.getEmitenteDestinario().setEmail(pessoaJuridica.getEmail());
			
		} else if (cota.getPessoa() instanceof PessoaFisica) {			
			PessoaFisica pessoaFisica = (PessoaFisica) cota.getPessoa();
			notaFiscal.getEmitenteDestinario().setNome(pessoaFisica.getNome());
			notaFiscal.getEmitenteDestinario().setEmail(pessoaFisica.getEmail());
		}
		
		notaFiscal.setDataEmissao(new Date());			
		
		return notaFiscal;

	}

	public static NotaFiscalNds montarEnderecoEmitenteDestinatarioEstoqueProduto(NotaFiscalNds notaFiscal, EstoqueProduto estoque) {
		
		notaFiscal.getEmitenteDestinario().setNome("XXXXXXXXX");
		notaFiscal.getEmitenteDestinario().setEmail("XXXXXXXX");
		
		return notaFiscal;
		
	}

	public static void montarEnderecoEmitenteDestinatario(NotaFiscal notaFiscal2, Cota cota) {
		
		if(notaFiscal2.getIdentificacaoDestinatario() == null) {
			notaFiscal2.setIdentificacaoDestinatario(new IdentificacaoDestinatario());
		}
		
		if(notaFiscal2.getIdentificacaoDestinatario().getEndereco() == null) {
			notaFiscal2.getIdentificacaoDestinatario().setEndereco(new NotaFicalEndereco());
		}
		
		if (cota.getPessoa() instanceof PessoaJuridica) {
			PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
			notaFiscal2.getIdentificacaoDestinatario().setNome(pessoaJuridica.getRazaoSocial());
			notaFiscal2.getIdentificacaoDestinatario().setEmail(pessoaJuridica.getEmail());
			
		} else if (cota.getPessoa() instanceof PessoaFisica) {			
			PessoaFisica pessoaFisica = (PessoaFisica) cota.getPessoa();
			notaFiscal2.getIdentificacaoDestinatario().setNome(pessoaFisica.getNome());
			notaFiscal2.getIdentificacaoDestinatario().setEmail(pessoaFisica.getEmail());
		}
		
		notaFiscal2.getIdentificacaoDestinatario().getEndereco().setId(cota.getEnderecoPrincipal().getEndereco().getId());
		
	}
	
	public static void montarEnderecoEmitenteDestinatario(NotaFiscal notaFiscal2, EstoqueProduto estoque) {
		
		if(notaFiscal2.getIdentificacaoDestinatario() == null) {
			notaFiscal2.setIdentificacaoDestinatario(new IdentificacaoDestinatario());
		}
		
		if(notaFiscal2.getIdentificacaoDestinatario().getEndereco() == null) {
			notaFiscal2.getIdentificacaoDestinatario().setEndereco(new NotaFicalEndereco());
		}
		
		
		
	}
	
}